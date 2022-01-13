package com.example.backend.user;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "roles")
@Data
public class Role extends BaseEntity{

    @Column(name = "name")
    private String name;
}
