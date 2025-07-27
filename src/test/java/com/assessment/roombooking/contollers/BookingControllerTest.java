
package com.assessment.roombooking.contollers;

import com.assessment.roombooking.controllers.BookingController;
import com.assessment.roombooking.dto.BookingDto;
import com.assessment.roombooking.models.Booking;
import com.assessment.roombooking.services.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService roomBookingService;

    @InjectMocks
    private BookingController bookingController;

    private BookingDto validBookingDto;
    private Booking validBooking;
    private final String validStartTime = "2024-01-01T10:00:00";
    private final String validEndTime = "2024-01-01T11:00:00";

    @BeforeEach
    void setUp() {
        validBookingDto = new BookingDto();
        validBookingDto.setRoomId(1L);
        validBookingDto.setUserName("Test User");
        validBookingDto.setStartTime(validStartTime);
        validBookingDto.setEndTime(validEndTime);

        validBooking = new Booking();
        validBooking.setId(1L);
        validBooking.setRoomId(1L);
        validBooking.setUserName("Test User");
        validBooking.setStartTime(LocalDateTime.parse(validStartTime));
        validBooking.setEndTime(LocalDateTime.parse(validEndTime));
    }

    @Test
    void createBooking_Success() {
        when(roomBookingService.createBooking(any(Booking.class))).thenReturn(1L);

        ResponseEntity<?> response = bookingController.createBooking(validBookingDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Booking created with ID: 1", response.getBody());
        verify(roomBookingService).createBooking(any(Booking.class));
    }

    @Test
    void createBooking_InvalidInput() {
        when(roomBookingService.createBooking(any(Booking.class)))
                .thenThrow(new IllegalArgumentException("Invalid booking"));

        ResponseEntity<?> response = bookingController.createBooking(validBookingDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Invalid booking"));
        verify(roomBookingService).createBooking(any(Booking.class));
    }

    @Test
    void createBooking_ServerError() {
        when(roomBookingService.createBooking(any(Booking.class)))
                .thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = bookingController.createBooking(validBookingDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Server error"));
        verify(roomBookingService).createBooking(any(Booking.class));
    }

    @Test
    void getBookings_Success_NoParameters() {
        List<Booking> bookings = Arrays.asList(validBooking);
        when(roomBookingService.getAllBookings(null, null, null)).thenReturn(bookings);

        ResponseEntity<?> response = bookingController.getBookings(null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookings, response.getBody());
        verify(roomBookingService).getAllBookings(null, null, null);
    }

    @Test
    void getBookings_Success_WithParameters() {
        List<Booking> bookings = Arrays.asList(validBooking);
        when(roomBookingService.getAllBookings(
                eq(1L),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(bookings);

        ResponseEntity<?> response = bookingController.getBookings(1L, validStartTime, validEndTime);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookings, response.getBody());
        verify(roomBookingService).getAllBookings(
                eq(1L),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        );
    }

    @Test
    void getBookings_ServerError() {
        when(roomBookingService.getAllBookings(any(), any(), any()))
                .thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = bookingController.getBookings(null, null, null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Server error"));
        verify(roomBookingService).getAllBookings(null, null, null);
    }

    @Test
    void deleteBookingById_Success() {
        doNothing().when(roomBookingService).deleteBookingById(1L);

        ResponseEntity<?> response = bookingController.deleteBookingById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Booking deleted successfully.", response.getBody());
        verify(roomBookingService).deleteBookingById(1L);
    }

    @Test
    void deleteBookingById_NotFound() {
        doThrow(new IllegalArgumentException("Booking not found"))
                .when(roomBookingService).deleteBookingById(1L);

        ResponseEntity<?> response = bookingController.deleteBookingById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Booking not found", response.getBody());
        verify(roomBookingService).deleteBookingById(1L);
    }

    @Test
    void deleteBookingById_ServerError() {
        doThrow(new RuntimeException("Server error"))
                .when(roomBookingService).deleteBookingById(1L);

        ResponseEntity<?> response = bookingController.deleteBookingById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Server error"));
        verify(roomBookingService).deleteBookingById(1L);
    }

    @Test
    void createBooking_InvalidDateFormat() {
        BookingDto invalidDto = new BookingDto();
        invalidDto.setRoomId(1L);
        invalidDto.setUserName("Test User");
        invalidDto.setStartTime("invalid-date");
        invalidDto.setEndTime("invalid-date");

        ResponseEntity<?> response = bookingController.createBooking(invalidDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error creating booking"));
        verify(roomBookingService, never()).createBooking(any(Booking.class));
    }

    @Test
    void getBookings_InvalidDateFormat() {
        ResponseEntity<?> response = bookingController.getBookings(1L, "invalid-date", "invalid-date");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Error retrieving bookings"));
        verify(roomBookingService, never()).getAllBookings(any(), any(), any());
    }
}