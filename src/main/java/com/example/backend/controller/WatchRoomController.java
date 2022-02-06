package com.example.backend.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.backend.dto.WatchRoomDto;
import com.example.backend.model.movie.Movie;
import com.example.backend.model.movie.MovieRepository;
import com.example.backend.model.watchRoom.WatchRoom;
import com.example.backend.model.watchRoom.WatchRoomRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/watcRooms")
@RequiredArgsConstructor
public class WatchRoomController {

    private final WatchRoomRepository watchRoomRepository;
    private final MovieRepository movieRepository;

    @GetMapping
    public List<WatchRoom> getAll() {
        return watchRoomRepository.findAll();
    }

    @GetMapping("{id}")
    public WatchRoom getOne(@PathVariable("id") UUID id) {
        return watchRoomRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No room found with id " + id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public WatchRoom createRoom(@RequestBody WatchRoomDto watchRoomDto) {
        WatchRoom watchRoom = new WatchRoom();
        watchRoom.setRoomName(watchRoomDto.getRoomName());

        Optional<Movie> optionalMovie = movieRepository.findById(watchRoomDto.getMovieId());
        if (optionalMovie.isPresent()) {
            watchRoom.setMovie(optionalMovie.get());
        }

        return watchRoomRepository.save(watchRoom);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public WatchRoom updateRoom(@PathVariable("id") UUID id, @RequestBody WatchRoomDto watchRoomDto) {
        Optional<WatchRoom> optionalRoom = watchRoomRepository.findById(id);
        if (optionalRoom.isPresent()) {
            WatchRoom watchRoom = new WatchRoom();
            watchRoom.setRoomName(watchRoomDto.getRoomName());

            Optional<Movie> optionalMovie = movieRepository.findById(watchRoomDto.getMovieId());
            if (optionalMovie.isPresent()) {
                watchRoom.setMovie(optionalMovie.get());
            }

            return watchRoomRepository.save(watchRoom);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No room found with id " + id);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public void deleteOne(@PathVariable("id") UUID id) {
        Optional<WatchRoom> optionalRoom = watchRoomRepository.findById(id);
        if (optionalRoom.isPresent()) {
            watchRoomRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No room found with id " + id);
        }
    }

}
