package com.example.backend.dto;

import java.util.List;

import com.example.backend.model.movie.Movie;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MoviesDto {
    private List<Movie> movies;
    private int totalPages;
}
