package com.guterres.adriano.libraryapi.model;


import com.guterres.adriano.libraryapi.model.entities.Book;
import com.guterres.adriano.libraryapi.model.repositories.IBookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    IBookRepository iBookRepository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o ISBN informado")
    public void returnTrueWhenIsbnExists(){

        String isbn = "1213";
        Book book = Book.builder().title("Title").author("Author").isbn(isbn).id(0).build();
        testEntityManager.persist(book);

        boolean exists = iBookRepository.existsByIsbn(isbn);

        assertThat(exists).isTrue();

    }
}
