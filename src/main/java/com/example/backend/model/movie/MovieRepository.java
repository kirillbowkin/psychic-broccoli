package com.example.backend.model.movie;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, UUID> {
    @Query("SELECT m FROM movies m WHERE search_title(:title) = true")
    Optional<List<Movie>> searchByTitle(@Param("title") String title);
}
