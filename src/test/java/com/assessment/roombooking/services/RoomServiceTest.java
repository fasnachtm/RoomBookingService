package com.assessment.roombooking.services;

import com.assessment.roombooking.dto.RoomDto;
import com.assessment.roombooking.models.Room;
import com.assessment.roombooking.repositories.BookingRepository;
import com.assessment.roombooking.repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @InjectMocks
    private RoomService roomService;

    private Room room;
    private RoomDto roomDto;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setId(1L);
        room.setName("Test Room");

        roomDto = new RoomDto();
        roomDto.setName("Test Room");

    }

    @Test
    void createRoom_ShouldReturnRoomId() {
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Long roomId = roomService.createRoom(roomDto);

        assertEquals(1L, roomId);
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void getAllRooms_ShouldReturnListOfRooms() {
        when(roomRepository.findAll()).thenReturn(List.of(room));

        List<Room> rooms = roomService.getAllRooms();

        assertFalse(rooms.isEmpty());
        assertEquals(1, rooms.size());
        verify(roomRepository).findAll();
    }

    @Test
    void getRoomById_WhenRoomExists_ShouldReturnRoom() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Optional<Room> foundRoom = roomService.getRoomById(1L);

        assertTrue(foundRoom.isPresent());
        assertEquals(room.getId(), foundRoom.get().getId());
        verify(roomRepository).findById(1L);
    }

    @Test
    void getRoomById_WhenRoomDoesNotExist_ShouldReturnEmpty() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Room> foundRoom = roomService.getRoomById(1L);

        assertTrue(foundRoom.isEmpty());
        verify(roomRepository).findById(1L);
    }

    @Test
    void deleteRoomById_WhenRoomExists_ShouldDeleteRoom() {
        when(roomRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> roomService.deleteRoomById(1L));

        verify(roomRepository).existsById(1L);
        verify(roomRepository).deleteById(1L);
    }

    @Test
    void deleteRoomById_WhenRoomDoesNotExist_ShouldThrowException() {
        when(roomRepository.existsById(1L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> roomService.deleteRoomById(1L)
        );

        assertEquals("Room with id 1 does not exist.", exception.getMessage());
        verify(roomRepository).existsById(1L);
        verify(roomRepository, never()).deleteById(any());
    }

}
