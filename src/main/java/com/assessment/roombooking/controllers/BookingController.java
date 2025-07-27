package com.assessment.roombooking.controllers;

import com.assessment.roombooking.dto.BookingDto;
import com.assessment.roombooking.models.Booking;
import com.assessment.roombooking.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // POST endpoint to create a booking
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> createBooking(
            @RequestBody() BookingDto bookingDto) {
        try {
            LocalDateTime parsedStartTime = bookingDto.getStartTime() != null ? LocalDateTime.parse(bookingDto.getStartTime()) : null;
            LocalDateTime parsedEndTime = bookingDto.getEndTime() != null ? LocalDateTime.parse(bookingDto.getEndTime()) : null;

            Booking booking = new Booking(bookingDto.getRoomId(),bookingDto.getUserName(),parsedStartTime,parsedEndTime);
            Long bookingId = bookingService.createBooking(booking);
            return ResponseEntity.ok("Booking created with ID: " + bookingId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error creating booking: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating booking: " + e.getMessage());
        }
    }

    // GET endpoint to retrieve all bookings
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getBookings(
            @RequestParam(value = "roomId", required = false) Long roomId,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime
    ) {
        try {
            LocalDateTime parsedStartTime = startTime != null ? LocalDateTime.parse(startTime) : null;
            LocalDateTime parsedEndTime = endTime != null ? LocalDateTime.parse(endTime) : null;
            return ResponseEntity.ok(bookingService.getAllBookings(roomId, parsedStartTime, parsedEndTime));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving bookings: " + e.getMessage());
        }
    }

    // DELETE endpoint to delete a booking by ID
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<?> deleteBookingById(@PathVariable("id") Long id) {
        try {
            bookingService.deleteBookingById(id);
            return ResponseEntity.ok("Booking deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting booking: " + e.getMessage());
        }
    }
}
