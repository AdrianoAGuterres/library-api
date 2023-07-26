package com.guterres.adriano.libraryapi.api.models.dtos;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class BookDTO {
    private String title;
    private String author;
    private String isbn;
    private long id;
}
