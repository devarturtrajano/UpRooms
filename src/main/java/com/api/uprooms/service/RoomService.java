package com.api.uprooms.service;

import com.api.uprooms.dto.RoomDTO;
import com.api.uprooms.model.Room;
import com.api.uprooms.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Transactional
    public RoomDTO createRoom(RoomDTO dto) {
        if (roomRepository.existsByNumber(dto.number())) {
            throw new IllegalArgumentException("Já existe uma sala cadastrada com o número " + dto.number());
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
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada com ID: " + id));
    }

    @Transactional
    public void toggleRoomStatus(Long id) {
        Room room = findEntityById(id);
        room.setActive(!room.isActive());
        roomRepository.save(room);
    }
}