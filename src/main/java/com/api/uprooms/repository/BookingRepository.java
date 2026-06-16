package com.api.uprooms.repository;

import com.api.uprooms.model.Booking;
import com.api.uprooms.model.enums.EnumBookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.room.id = :roomId " +
            "AND b.status = :status " +
            "AND (:startTime < b.endTime AND :endTime > b.startTime)")
    boolean existsOverlappingBooking(
            @Param("roomId") Long roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") EnumBookingStatus.BookingStatus status
    );

    List<Booking> findByUserId(Long userId);
}