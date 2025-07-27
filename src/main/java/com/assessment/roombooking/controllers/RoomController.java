package com.assessment.roombooking.controllers;

import com.assessment.roombooking.dto.RoomDto;
import com.assessment.roombooking.models.Room;
import com.assessment.roombooking.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createRoom(
            @RequestBody() RoomDto room
    ) {
        try {
            Long roomId = roomService.createRoom(room);
            return ResponseEntity.ok("Room created with ID: " + roomId);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Room data is required.");
        }

        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating room: " + e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllRooms() {
        try {
            return ResponseEntity.ok(roomService.getAllRooms());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving rooms: " + e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<?> getRoomById(@Param("id") Long id) {
        try {
            Optional<Room> room = roomService.getRoomById(id);
            if (room.isPresent()) {
                return ResponseEntity.ok(room);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room with id " + id + " not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving room: " + e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<?> deleteRoomById(@Param("id") Long id) {
        try {
            roomService.deleteRoomById(id);
            return ResponseEntity.ok("Room deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting room: " + e.getMessage());
        }
    }
}
