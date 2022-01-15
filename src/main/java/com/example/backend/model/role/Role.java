package com.example.backend.model.role;

import com.example.backend.model.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "roles")
@Data
public class Role extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color = "green";
}
