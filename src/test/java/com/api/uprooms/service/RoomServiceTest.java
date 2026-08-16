package com.api.uprooms.service;

import com.api.uprooms.dto.RoomDTO;
import com.api.uprooms.model.Room;
import com.api.uprooms.model.User;
import com.api.uprooms.model.enums.EnumUserRole;
import com.api.uprooms.repository.RoomRepository;
import com.api.uprooms.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private RoomRepository mockRoomRepository;

    @Mock
    private UserRepository mockUserRepository;

    @InjectMocks
    private RoomService mockRoomService;

    private Room sampleRoom;
    private User sampleUser;
    private User sampleAdminUser;
    private RoomDTO roomDTO;

    @BeforeEach
    void setUp(){
        sampleRoom = new Room(1L, 101, 10, true);
        sampleUser = new User(1L, "John Doe", "john@email.com", "password", EnumUserRole.UserRole.COLLABORATOR, new ArrayList<>());
        sampleAdminUser = new User(1L, "AdminName", "admin@email.com", "adminPassword", EnumUserRole.UserRole.ADMIN, new ArrayList<>());
        roomDTO = new RoomDTO(sampleRoom.getId(), sampleRoom.getNumber(), sampleRoom.getCapacity(), sampleRoom.isActive());
    }

    @Test
    @DisplayName("Should create a room successfully")
    void should_create_room_successfully(){

        Room savedRoom = sampleRoom;

        Mockito.when(mockRoomRepository.existsByNumber(roomDTO.number())).thenReturn(false);
        Mockito.when(mockUserRepository.findById(sampleAdminUser.getId())).thenReturn(java.util.Optional.of(sampleAdminUser));
        Mockito.when(mockRoomRepository.save(Mockito.any(Room.class))).thenReturn(savedRoom);

        RoomDTO result = mockRoomService.createRoom(roomDTO, sampleAdminUser.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(roomDTO.number(), result.number());
        Assertions.assertEquals(roomDTO.id(), result.id());
        Assertions.assertEquals(roomDTO.capacity(), result.capacity());
        Assertions.assertEquals(roomDTO.isActive(), result.isActive());
        Assertions.assertEquals(roomDTO, result);
        Mockito.verify(mockRoomRepository, Mockito.times(1)).save(Mockito.any(Room.class));
    }

    @Test
    @DisplayName("Should return a Illegal Argument exception when a user tries to create a room which number already exists")
    void should_return_illegal_argument_exception_when_a_user_tries_to_create_a_room_which_number_already_exists(){
        Mockito.when(mockRoomRepository.existsByNumber(roomDTO.number())).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> mockRoomService.createRoom(roomDTO, sampleAdminUser.getId()));
        Mockito.verify(mockRoomRepository, Mockito.times(1)).existsByNumber(roomDTO.number());
    }

    @Test
    @DisplayName("Should return a Illegal State exception when there is a attempt to create a room with a not found user")
    void should_return_illegal_state_exception_when_there_is_a_attempt_to_create_a_room_with_a_not_found_user(){
        Mockito.when(mockUserRepository.findById(sampleAdminUser.getId())).thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(IllegalStateException.class, () -> mockRoomService.createRoom(roomDTO, sampleAdminUser.getId()));
        Mockito.verify(mockUserRepository, Mockito.times(1)).findById(sampleAdminUser.getId());
    }

    @Test
    @DisplayName("Should return a Security exception when there is a attempt to create a room with a non admin user")
    void should_return_illegal_state_exception_when_there_is_a_attempt_to_create_a_room_with_a_not_admin_user(){
        Mockito.when(mockUserRepository.findById(sampleUser.getId())).thenReturn(java.util.Optional.of(sampleUser));

        Assertions.assertThrows(SecurityException.class, () -> mockRoomService.createRoom(roomDTO, sampleUser.getId()));
        Mockito.verify(mockUserRepository, Mockito.times(1)).findById(sampleUser.getId());
    }

    @Test
    @DisplayName("Should find all active rooms successfully")
    void should_return_all_active_rooms_list(){
        Room sampleRoom2 = new Room(2L, 102, 20, true);
        Mockito.when(mockRoomRepository.findByIsActiveTrue()).thenReturn(java.util.List.of(sampleRoom, sampleRoom2));

        java.util.List<RoomDTO> result = mockRoomService.findAllActiveRooms();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(sampleRoom.getId(), result.get(0).id());
        Assertions.assertEquals(sampleRoom2.getId(), result.get(1).id());
        Mockito.verify(mockRoomRepository, Mockito.times(1)).findByIsActiveTrue();
    }

    @Test
    @DisplayName("Should find a room by Id successfully")
    void should_find_room_by_id_successfully(){
        Mockito.when(mockRoomRepository.findById(sampleRoom.getId())).thenReturn(java.util.Optional.of(sampleRoom));

        Room result = mockRoomService.findEntityById(sampleRoom.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(sampleRoom.getId(), result.getId());
        Assertions.assertEquals(sampleRoom, result);
        Mockito.verify(mockRoomRepository, Mockito.times(1)).findById(sampleRoom.getId());
    }

    @Test
    @DisplayName("Should return a Illegal Argument exception when a user tries to find a room which does not exists")
    void should_return_illegal_argument_exception_when_a_user_tries_to_find_a_room_which_does_not_exists(){
        Mockito.when(mockRoomRepository.findById(sampleRoom.getId())).thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> mockRoomService.findEntityById(sampleRoom.getId()));
        Mockito.verify(mockRoomRepository, Mockito.times(1)).findById(sampleRoom.getId());
    }

    @Test
    @DisplayName("Should toggle a room status successfully")
    void should_toggle_room_status_successfully(){
        Mockito.when(mockUserRepository.findById(sampleAdminUser.getId())).thenReturn(java.util.Optional.of(sampleAdminUser));
        Mockito.when(mockRoomRepository.findById(sampleRoom.getId())).thenReturn(java.util.Optional.of(sampleRoom));
        Mockito.when(mockRoomRepository.save(sampleRoom)).thenReturn(sampleRoom);

        mockRoomService.toggleRoomStatus(sampleAdminUser.getId(), sampleRoom.getId());
        Assertions.assertFalse(sampleRoom.isActive());
        Mockito.verify(mockRoomRepository, Mockito.times(1)).save(sampleRoom);
    }

    @Test
    @DisplayName("Should return a Illegal Argument exception when a user tries to toggle a room status which does not exists")
    void should_return_illegal_argument_exception_when_a_user_tries_to_toggle_a_room_status_which_does_not_exists(){
        Mockito.when(mockUserRepository.findById(sampleAdminUser.getId())).thenReturn(java.util.Optional.of(sampleAdminUser));
        Mockito.when(mockRoomRepository.findById(sampleRoom.getId())).thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> mockRoomService.toggleRoomStatus(sampleAdminUser.getId(), sampleRoom.getId()));
        Mockito.verify(mockRoomRepository, Mockito.times(1)).findById(sampleRoom.getId());
    }

    @Test
    @DisplayName("Should return a Security exception when a non admin user tries to toggle a room status")
    void should_return_security_exception_when_a_non_admin_user_tries_to_toggle_a_room_status(){
        Mockito.when(mockUserRepository.findById(sampleUser.getId())).thenReturn(java.util.Optional.of(sampleUser));

        Assertions.assertThrows(SecurityException.class, () -> mockRoomService.toggleRoomStatus(sampleUser.getId(), sampleRoom.getId()));
        Mockito.verify(mockUserRepository, Mockito.times(1)).findById(sampleUser.getId());
    }
}
