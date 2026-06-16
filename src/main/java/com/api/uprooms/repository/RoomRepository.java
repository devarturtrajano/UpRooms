package com.api.uprooms.repository;

import com.api.uprooms.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByIsActiveTrue();
    Optional<Room> findByNumber(Integer number);
    boolean existsByNumber(Integer number);
}