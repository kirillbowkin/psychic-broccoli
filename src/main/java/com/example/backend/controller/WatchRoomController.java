package com.example.backend.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.example.backend.dto.WatchRoomDto;
import com.example.backend.model.movie.Movie;
import com.example.backend.model.movie.MovieRepository;
import com.example.backend.model.user.User;
import com.example.backend.model.user.UserRepository;
import com.example.backend.model.watchRoom.WatchRoom;
import com.example.backend.model.watchRoom.WatchRoomRepository;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/watchRooms")
@RequiredArgsConstructor
@Slf4j
public class WatchRoomController {

    private final WatchRoomRepository watchRoomRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @GetMapping
    public List<WatchRoom> getAll() {
        return watchRoomRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @GetMapping("{id}")
    public WatchRoom getOne(@PathVariable("id") UUID id) {
        return watchRoomRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No room found with id " + id));
    }

    @PostMapping("/join/{id}")
    @PreAuthorize("hasRole('USER')")
    public void join(@PathVariable("id") UUID id, Principal principal) {
        Optional<WatchRoom> optionalRoom = watchRoomRepository.findById(id);
        if (optionalRoom.isPresent()) {
            User user = userRepository
                    .findByUsername(principal.getName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

            WatchRoom watchRoom = optionalRoom.get();
            Set<User> users = watchRoom.getUsers();

            if (users.contains(user)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already joined the room");
            } else {
                users.add(user);
                watchRoom.setUsers(users);
                watchRoomRepository.save(watchRoom);
                log.info("User {} joined the room {} (id={})", user.getProfileName(), watchRoom.getRoomName(),
                        watchRoom.getId());
            }

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No room found with id " + id);
        }

    }

    @DeleteMapping("/leave/{id}")
    @PreAuthorize("hasRole('USER')")
    public void leave(@PathVariable("id") UUID id, Principal principal) {
        Optional<WatchRoom> optionalRoom = watchRoomRepository.findById(id);
        if (optionalRoom.isPresent()) {
            User user = userRepository
                    .findByUsername(principal.getName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

            WatchRoom watchRoom = optionalRoom.get();
            Set<User> users = watchRoom.getUsers();

            if (!users.contains(user)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already leaved the room");
            } else {
                users.remove(user);
                watchRoom.setUsers(users);
                watchRoomRepository.save(watchRoom);
                log.info("User {} leaved the room {} (id={})", user.getProfileName(), watchRoom.getRoomName(),
                        watchRoom.getId());
            }

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No room found with id " + id);
        }

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public WatchRoom createRoom(@RequestBody WatchRoomDto watchRoomDto, Principal principal) {
        WatchRoom watchRoom = new WatchRoom();
        watchRoom.setRoomName(watchRoomDto.getRoomName());

        // Optional<Movie> optionalMovie =
        // movieRepository.findById(watchRoomDto.getMovieId());
        // if (optionalMovie.isPresent()) {
        // watchRoom.setMovie(optionalMovie.get());
        // }

        User user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        watchRoom.setHost(user);

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
