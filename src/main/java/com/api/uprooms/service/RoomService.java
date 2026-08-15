package com.api.uprooms.service;

import com.api.uprooms.dto.RoomDTO;
import com.api.uprooms.model.Room;
import com.api.uprooms.model.enums.EnumUserRole;
import com.api.uprooms.repository.RoomRepository;
import com.api.uprooms.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public RoomService(RoomRepository roomRepository, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RoomDTO createRoom(RoomDTO dto, Long userId) {
        if (roomRepository.existsByNumber(dto.number())) {
            throw new IllegalArgumentException("Already exists a room with number: " + dto.number());
        }

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        if (user.getRole() != EnumUserRole.UserRole.ADMIN) {
            throw new SecurityException("Only admins can perform this action.");
        }

        Room room = new Room();
        room.setNumber(dto.number());
        room.setCapacity(dto.capacity());
        room.setActive(dto.isActive());

        room = roomRepository.save(room);
        return new RoomDTO(room);
    }

    public List<RoomDTO> findAllActiveRooms() {
        return roomRepository.findByIsActiveTrue()
                .stream()
                .map(RoomDTO::new)
                .toList();
    }

    public Room findEntityById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + id));
    }

    @Transactional
    public void toggleRoomStatus(Long userId, Long id) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (user.getRole() != EnumUserRole.UserRole.ADMIN) {
            throw new SecurityException("Only admins can perform this action.");
        }

        Room room = findEntityById(id);
        room.setActive(!room.isActive());
        roomRepository.save(room);
    }
}