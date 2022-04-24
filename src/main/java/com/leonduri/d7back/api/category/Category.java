package com.leonduri.d7back.api.category;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String kind;
}
