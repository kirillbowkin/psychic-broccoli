package com.example.backend.model.watchRoom;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchRoomRepository extends JpaRepository<WatchRoom, UUID> {

}
