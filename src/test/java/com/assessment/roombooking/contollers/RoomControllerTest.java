
package com.assessment.roombooking.contollers;

import com.assessment.roombooking.controllers.RoomController;
import com.assessment.roombooking.dto.RoomDto;
import com.assessment.roombooking.models.Room;
import com.assessment.roombooking.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    private RoomDto validRoomDto;
    private Room    validRoom;
    private List<Room> roomList;

    @BeforeEach
    void setUp() {
        validRoomDto = new RoomDto();
        validRoomDto.setName("Test Room");
        validRoomDto.setLocation("Test Location");

        RoomDto secondRoomDto = new RoomDto();
        secondRoomDto.setName("Second Room");
        secondRoomDto.setLocation("Second Location");

        validRoom = new Room();
        validRoom.setId(1L);
        validRoom.setName(validRoomDto.getName());
        validRoom.setLocation(validRoomDto.getLocation());

        Room secondRoom = new Room();
        secondRoom.setId(2L);
        secondRoom.setName(secondRoom.getName());
        secondRoom.setLocation(secondRoom.getLocation());

        roomList = Arrays.asList(validRoom, secondRoom);
    }

    @Test
    void createRoom_Success() {
        when(roomService.createRoom(any(RoomDto.class))).thenReturn(1L);

        ResponseEntity<?> response = roomController.createRoom(validRoomDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Room created with ID: 1", response.getBody());
        verify(roomService).createRoom(validRoomDto);
    }

    @Test
    void createRoom_ServerError() {
        when(roomService.createRoom(any(RoomDto.class)))
                .thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = roomController.createRoom(validRoomDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Server error"));
        verify(roomService).createRoom(validRoomDto);
    }

    @Test
    void getAllRooms_Success() {
        when(roomService.getAllRooms()).thenReturn(roomList);

        ResponseEntity<?> response = roomController.getAllRooms();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roomList, response.getBody());
        verify(roomService).getAllRooms();
    }

    @Test
    void getAllRooms_ServerError() {
        when(roomService.getAllRooms())
                .thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = roomController.getAllRooms();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Server error"));
        verify(roomService).getAllRooms();
    }

    @Test
    void getRoomById_Success() {
        when(roomService.getRoomById(1L)).thenReturn(Optional.of(validRoom));

        ResponseEntity<?> response = roomController.getRoomById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(validRoom, ((Optional) response.getBody()).get());
        verify(roomService).getRoomById(1L);
    }

    @Test
    void getRoomById_NotFound() {
        when(roomService.getRoomById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = roomController.getRoomById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(roomService).getRoomById(1L);
    }

    @Test
    void getRoomById_ServerError() {
        when(roomService.getRoomById(1L))
                .thenThrow(new RuntimeException("Server error"));

        ResponseEntity<?> response = roomController.getRoomById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Server error"));
        verify(roomService).getRoomById(1L);
    }

    @Test
    void deleteRoomById_Success() {
        doNothing().when(roomService).deleteRoomById(1L);

        ResponseEntity<?> response = roomController.deleteRoomById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Room deleted successfully.", response.getBody());
        verify(roomService).deleteRoomById(1L);
    }

    @Test
    void deleteRoomById_NotFound() {
        doThrow(new IllegalArgumentException("Room not found"))
                .when(roomService).deleteRoomById(1L);

        ResponseEntity<?> response = roomController.deleteRoomById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Room not found", response.getBody());
        verify(roomService).deleteRoomById(1L);
    }

    @Test
    void deleteRoomById_ServerError() {
        doThrow(new RuntimeException("Server error"))
                .when(roomService).deleteRoomById(1L);

        ResponseEntity<?> response = roomController.deleteRoomById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Server error"));
        verify(roomService).deleteRoomById(1L);
    }

    @Test
    void createRoom_NullRoom() {
        doThrow(new RuntimeException("Invalid booking"))
                .when(roomService).createRoom(isNull());

        ResponseEntity<?> response = roomController.createRoom(null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Invalid booking"));
    }

    @Test
    void createRoom_EmptyRoom() {
        RoomDto newRoomDto = new RoomDto();
        when(roomService.createRoom(any(RoomDto.class)))
                .thenReturn(1L);

        ResponseEntity<?> response = roomController.createRoom(newRoomDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Room created with ID: 1", response.getBody());
        verify(roomService).createRoom(newRoomDto);
    }
}