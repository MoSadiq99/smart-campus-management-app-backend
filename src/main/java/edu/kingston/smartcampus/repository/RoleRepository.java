package edu.kingston.smartcampus.repository;

import edu.kingston.smartcampus.model.enums.RoleName;
import edu.kingston.smartcampus.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleName roleName);
}