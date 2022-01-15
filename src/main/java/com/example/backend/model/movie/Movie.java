package com.example.backend.model.movie;

import com.example.backend.model.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "movies")
@Data
public class Movie extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "director")
    private String director;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "link")
    private String link;

    @Column(name = "description")
    private String description;

    @Column(name = "genre")
    private String genre;

    @Column(name = "duration")
    private String duration;

    @Column(name = "year")
    private Integer year;
}

