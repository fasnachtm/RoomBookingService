package com.assessment.roombooking.services;


import com.assessment.roombooking.dto.BookingDto;
import com.assessment.roombooking.models.Booking;
import com.assessment.roombooking.models.Room;
import com.assessment.roombooking.repositories.BookingRepository;
import com.assessment.roombooking.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    public static final String ERROR_BOOKING_OVERLAP = "The room is already booked for the specified time.";
    public static final String ERROR_MESSAGE_INVALID_TIME_RANGE = "Start time must come before end time.";
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public Long createBooking(BookingDto bookingDto) {
        try {
            Booking booking = checkAndConvertToBooking(bookingDto);
            Booking savedBooking = bookingRepository.save(booking);
            return savedBooking.getId();
        } catch (DateTimeParseException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Bad booking request: " + e.getMessage());
        }
    }



    public Long createBooking(Booking booking) {
        // change
        if ((booking.getStartTime() == null) || (booking.getEndTime() == null)  ||
                (booking.getEndTime().isBefore(booking.getStartTime()))) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_TIME_RANGE);
        }
        // Check if the room exists
        Optional<Room> roomOptional = roomRepository.findById(booking.getRoomId());
        if (roomOptional.isEmpty()) {
            throw new IllegalArgumentException("Room with id " + booking.getRoomId() + " does not exist.");
        }
        // Check for overlapping bookings
        List<Booking> existingBookings = bookingRepository.findByRoomIdAndRange(booking.getRoomId(), booking.getStartTime(), booking.getEndTime());
        if (!existingBookings.isEmpty())
            throw new IllegalArgumentException(ERROR_BOOKING_OVERLAP);

            // Save the booking
        Booking savedBooking = bookingRepository.save(booking);
        return savedBooking.getId();
    }

    public List<Booking> getAllBookings(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        if (roomId != null && startTime != null && endTime != null) {
            return bookingRepository.findByRoomIdAndRange(roomId, startTime, endTime);
        } else if (roomId != null) {
            return bookingRepository.findByRoomId(roomId);
        } else if (startTime != null && endTime != null) {
            return bookingRepository.findByRange(startTime, endTime);
        }
        return bookingRepository.findAll();
    }

    public void deleteBookingById(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Booking with id " + id + " does not exist.");
        }
    }

    public LocalDateTime parseMultipleFormats(String dateStr) {
        List<DateTimeFormatter> formatters = Arrays.asList(
                DateTimeFormatter.ISO_LOCAL_DATE_TIME,
                DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")
        );

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                continue;
            }
        }
        throw new DateTimeParseException("Unable to parse date: " + dateStr, dateStr, 0);
    }

    private Booking checkAndConvertToBooking(BookingDto bookingDto) {
        // validate time range
        LocalDateTime startTime = parseMultipleFormats(bookingDto.getStartTime());
        LocalDateTime endTime = parseMultipleFormats(bookingDto.getEndTime());

        if ((startTime == null) || (endTime == null)  ||
                (endTime.isBefore(startTime))) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_TIME_RANGE);
        }
        // Check if the room exists
        Optional<Room> roomOptional = roomRepository.findById(bookingDto.getRoomId());
        if (roomOptional.isEmpty()) {
            throw new IllegalArgumentException("Room with id " + bookingDto.getRoomId() + " does not exist.");
        }
        // Check for overlapping bookings
        List<Booking> existingBookings = bookingRepository.findByRoomIdAndRange(bookingDto.getRoomId(), startTime, endTime);
        if (!existingBookings.isEmpty())
            throw new IllegalArgumentException(ERROR_BOOKING_OVERLAP);

        return new Booking(bookingDto.getRoomId(), bookingDto.getUserName(), startTime, endTime);
    }
}
