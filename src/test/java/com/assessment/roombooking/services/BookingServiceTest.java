package com.assessment.roombooking.services;

import com.assessment.roombooking.models.Booking;
import com.assessment.roombooking.models.Room;
import com.assessment.roombooking.repositories.BookingRepository;
import com.assessment.roombooking.repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @InjectMocks
    private RoomService roomService;

    private Room room;
    private Booking booking;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(1L);
        room.setName("Test Room");

        booking = new Booking();
        booking.setId(1L);
        booking.setRoomId(room.getId());
        booking.setStartTime(LocalDateTime.now());
        booking.setEndTime(LocalDateTime.now().plusHours(1));
    }

    @Test
    void createBooking_ValidBooking_ShouldReturnBookingId() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(bookingRepository.findByRoomIdAndRange(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Long bookingId = bookingService.createBooking(booking);

        assertEquals(1L, bookingId);
        verify(roomRepository).findById(1L);
        verify(bookingRepository).findByRoomIdAndRange(eq(1L), any(), any());
        verify(bookingRepository).save(booking);
    }

    @Test
    void createBooking_WithNullStartTime_ShouldThrowException() {
        booking.setStartTime(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.createBooking(booking)
        );

        assertEquals("Start time must come before end time.", exception.getMessage());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_WithEndTimeBeforeStartTime_ShouldThrowException() {
        booking.setStartTime(LocalDateTime.now());
        booking.setEndTime(LocalDateTime.now().minusHours(1));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.createBooking(booking)
        );

        assertEquals("Start time must come before end time.", exception.getMessage());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_WhenRoomDoesNotExist_ShouldThrowException() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.createBooking(booking)
        );

        assertEquals("Room with id 1 does not exist.", exception.getMessage());
        verify(roomRepository).findById(1L);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_WhenRoomAlreadyBooked_ShouldThrowException() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(bookingRepository.findByRoomIdAndRange(anyLong(), any(), any()))
                .thenReturn(List.of(booking));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.createBooking(booking)
        );

        assertEquals("The room is already booked for the specified time.", exception.getMessage());
        verify(roomRepository).findById(1L);
        verify(bookingRepository).findByRoomIdAndRange(eq(1L), any(), any());
        verify(bookingRepository, never()).save(any());
    }


}
