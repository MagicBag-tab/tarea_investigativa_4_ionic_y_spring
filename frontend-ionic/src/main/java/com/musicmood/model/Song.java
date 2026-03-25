package com.musicmood.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "songs")
@Data
@NoArgsConstructor
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 100)
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "El artista no puede estar vacío")
    @Size(max = 100)
    @Column(nullable = false)
    private String artist;

    @Size(max = 50)
    private String genre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Mood mood;

    @Size(max = 255)
    private String notes;
}