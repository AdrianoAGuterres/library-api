package com.guterres.adriano.libraryapi.model.repositories;

import com.guterres.adriano.libraryapi.model.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);
}
