package com.guterres.adriano.libraryapi.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table


public class Book {
    @Column
    private String title;
    @Column
    private String author;
    @Column
    private String isbn;
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
}
