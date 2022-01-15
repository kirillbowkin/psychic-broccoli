package com.example.backend.model.movie;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, UUID> {
}
