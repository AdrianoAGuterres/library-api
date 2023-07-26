package com.guterres.adriano.libraryapi.api.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Book {
    private String title;
    private String author;
    private String isbn;
    private long id;
}
