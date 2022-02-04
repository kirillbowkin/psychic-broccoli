package com.example.backend.model.movie;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, UUID> {
    @Query("SELECT m FROM movies m WHERE search_title(:title) = true")
    Page<Movie> searchByTitle(@Param("title") String title, Pageable page);
}
