package edu.kingston.smartcampus.model.enums;


public class ResourceEnums {
    public enum Status {
        Available,
        Reserved,
        Unavailable
    }
    
    public enum Type {
        Room,
        Equipment,
        Vehicle,
        Area,
        Other
    }
}