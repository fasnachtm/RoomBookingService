package com.assessment.roombooking.services;

import com.assessment.roombooking.dto.RoomDto;
import com.assessment.roombooking.models.Booking;
import com.assessment.roombooking.models.Room;
import com.assessment.roombooking.repositories.BookingRepository;
import com.assessment.roombooking.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public Long createRoom(RoomDto roomDto) {
        if (roomDto.getName() == null || roomDto.getName().isEmpty()) {
            throw new IllegalArgumentException("Room name cannot be null or empty.");
        }
        Room savedRoom = roomRepository.save(new Room(roomDto.getName(), roomDto.getLocation()));
        return savedRoom.getId();
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public void deleteRoomById(Long id) {
        if (roomRepository.existsById(id)) {
            List<Booking> bookings = bookingRepository.findByRoomId(id);
            bookings.forEach(booking -> bookingRepository.delete(booking));
            roomRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Room with id " + id + " does not exist.");
        }
    }


}
