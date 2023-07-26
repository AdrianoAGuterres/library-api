package com.guterres.adriano.libraryapi.api.models.repositories;

import com.guterres.adriano.libraryapi.api.models.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBookRepository extends JpaRepository<Book, Long> {
}
