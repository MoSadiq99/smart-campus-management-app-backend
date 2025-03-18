package edu.kingston.smartcampus.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import edu.kingston.smartcampus.controller.ResourceController;
import edu.kingston.smartcampus.dto.ReservationCreateDto;
import edu.kingston.smartcampus.dto.ReservationDto;
import edu.kingston.smartcampus.model.Event;
import edu.kingston.smartcampus.model.Lecture;
import edu.kingston.smartcampus.model.Reservation;
import edu.kingston.smartcampus.model.Resource;
import edu.kingston.smartcampus.model.user.Admin;
import edu.kingston.smartcampus.repository.EventRepository;
import edu.kingston.smartcampus.repository.LectureRepository;
import edu.kingston.smartcampus.repository.ReservationRepository;
import edu.kingston.smartcampus.repository.ResourceRepository;
import edu.kingston.smartcampus.repository.UserRepository;

public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationService reservationService;

    @InjectMocks
    private ResourceController resourceController;

    private ReservationCreateDto reservationCreateDto;
    private ReservationDto reservationDto;
    private Reservation existingReservation;
    private Admin admin;
    private Resource resource;
    private Lecture lecture;
    private Event event;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock data
        admin = new Admin();
        admin.setId(1L);
        resource = new Resource();
        resource.setResourceId(1L);
        lecture = new Lecture();
        lecture.setId(1L);
        event = new Event();
        event.setId(1L);

        reservationCreateDto = new ReservationCreateDto();
        reservationCreateDto.setResourceId(resource.getResourceId());
        reservationCreateDto.setLectureId(lecture.getId());
        reservationCreateDto.setEventId(event.getId());
        reservationCreateDto.setTitle("Conference Room Reservation");
        reservationCreateDto.setStartTime(LocalDateTime.now().plusHours(1));
        reservationCreateDto.setEndTime(LocalDateTime.now().plusHours(2));

        reservationDto = new ReservationDto();
        reservationDto.setTitle("Conference Room Reservation");
        reservationDto.setResourceId(resource.getResourceId());
        reservationDto.setStartTime(reservationCreateDto.getStartTime());
        reservationDto.setEndTime(reservationCreateDto.getEndTime());

        existingReservation = new Reservation();
        existingReservation.setReservationId(1L);
        existingReservation.setTitle("Existing Reservation");
        existingReservation.setResource(resource);
        existingReservation.setStartTime(LocalDateTime.now().plusHours(1));
        existingReservation.setEndTime(LocalDateTime.now().plusHours(2));
    }

    @Test
    void testCreateReservation() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(resourceRepository.findById(anyLong())).thenReturn(Optional.of(resource));
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(reservationRepository.existsByResourceAndTimeRange(any(), any(), any())).thenReturn(false);
        when(reservationRepository.saveAll(anyList())).thenReturn(Collections.singletonList(existingReservation));

        ReservationDto result = reservationService.createReservation(reservationCreateDto, 1L);

        assertNotNull(result);
        assertEquals(reservationCreateDto.getTitle(), result.getTitle());
        assertEquals(reservationCreateDto.getResourceId(), result.getResourceId());
    }

    @Test
    void testCreateReservationWithConflict() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(admin));
        when(resourceRepository.findById(anyLong())).thenReturn(Optional.of(resource));
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(reservationRepository.existsByResourceAndTimeRange(any(), any(), any())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            reservationService.createReservation(reservationCreateDto, 1L);
        });
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Resource is already reserved"));
    }

    @Test
    void testUpdateReservation() {
        ReservationCreateDto updateDto = new ReservationCreateDto();
        updateDto.setResourceId(resource.getResourceId());
        updateDto.setLectureId(lecture.getId());
        updateDto.setEventId(event.getId());
        updateDto.setTitle("Updated Reservation");
        updateDto.setStartTime(LocalDateTime.now().plusHours(3));
        updateDto.setEndTime(LocalDateTime.now().plusHours(4));

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(existingReservation));
        when(resourceRepository.findById(anyLong())).thenReturn(Optional.of(resource));
        when(lectureRepository.findById(anyLong())).thenReturn(Optional.of(lecture));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        reservationService.updateReservation(existingReservation.getReservationId(),
                updateDto);

        assertEquals(updateDto.getTitle(), existingReservation.getTitle());
        assertEquals(updateDto.getStartTime(), existingReservation.getStartTime());
        assertEquals(updateDto.getEndTime(), existingReservation.getEndTime());
    }

    @Test
    void testGetReservationsInRange() {
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now().plusDays(1);
        List<Reservation> reservations = Collections.singletonList(existingReservation);
        when(reservationRepository.findByTimeRange(from, to)).thenReturn(reservations);

        List<Reservation> result = reservationService.getReservationsInRange(from, to);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(existingReservation.getTitle(), result.get(0).getTitle());
    }

    @Test
    public void testDeleteReservation_Success() {
        Long reservationId = 1L;
        Reservation mockReservation = new Reservation();
        mockReservation.setReservationId(reservationId);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(mockReservation));
        doNothing().when(reservationRepository).delete(mockReservation);

        reservationService.deleteReservation(reservationId);

        verify(reservationRepository, times(1)).delete(mockReservation);
    }
}
