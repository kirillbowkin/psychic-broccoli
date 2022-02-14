package com.example.backend.model.watchRoom;

import java.time.LocalTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.example.backend.model.BaseEntity;
import com.example.backend.model.movie.Movie;
import com.example.backend.model.user.User;

import lombok.Data;

@Entity(name = "watch_rooms")
@Data
public class WatchRoom extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @OneToOne
    @JoinColumn(name = "host_id")
    private User host;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "current_movie_time")
    private LocalTime currenMovieTime;

    @OneToMany
    private Set<User> users;
}
