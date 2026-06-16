package com.api.uprooms.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer number;
    private Integer capacity;
    private boolean isActive;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    public Room() {}

    public Room(Long id, Integer number, Integer capacity, boolean isActive) {
        this.id = id;
        this.number = number;
        this.capacity = capacity;
        this.isActive = isActive;
    }

    public Long getId() { return id; }
    public Integer getNumber() { return number; }
    public void setNumber(Integer number) { this.number = number; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public List<Booking> getBookings() { return bookings; }
    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
}