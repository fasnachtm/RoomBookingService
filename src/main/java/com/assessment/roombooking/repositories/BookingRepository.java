package com.assessment.roombooking.repositories;

import com.assessment.roombooking.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Custom query to find overlapping bookings for a specific room
    @Query("SELECT b FROM Booking b WHERE b.roomId = :roomId " +
            "AND NOT(b.endTime <= :startTime OR b.startTime >= :endTime)")
    List<Booking> findByRoomIdAndRange(@Param("roomId") Long roomId,
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime);

    // Custom query to check if there is an overlapping booking for a room
    @Query("SELECT b FROM Booking b WHERE NOT(b.endTime <= :startTime OR b.startTime >= :endTime)")
    List<Booking> findByRange(@Param("startTime") LocalDateTime startTime,
                              @Param("endTime") LocalDateTime endTime);

    // query to find all bookings for a specific room
    @Query("SELECT b FROM Booking b WHERE b.roomId = :roomId")
    List<Booking> findByRoomId(@Param("roomId") Long roomId);

}
