package com.example.backend.controller;

import com.example.backend.model.movie.Movie;
import com.example.backend.model.movie.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    private final MovieRepository movieRepository;

    @GetMapping()
    public List<Movie> getAll(@RequestParam("page") int page) {
        Page<Movie> moviesPages = movieRepository.findAll(PageRequest.of(page, 10, Sort.Direction.DESC, "createdAt"));
        List<Movie> movies = moviesPages.getContent();
        return movies;
    }

    @GetMapping("/search")
    public List<Movie> search(@RequestParam("title") String title) {
        Optional<List<Movie>> search = movieRepository.searchByTitle(title);
        if(search.isPresent()) {
            return search.get();
        }

        return null;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public Movie addMovie(@RequestBody Movie movie) {
        return movieRepository.save(movie);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public Movie editMovie(@PathVariable("id") UUID id, @RequestBody Movie movie) {

        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isPresent()) {
            movie.setId(id);
            movieRepository.save(movie);
            return movie;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No movie was found with id " + id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public void deleteMovie(@PathVariable("id") UUID id) {
        if (movieRepository.findById(id).isPresent()) {
            movieRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No movie was found with id " + id);
        }

    }

}
