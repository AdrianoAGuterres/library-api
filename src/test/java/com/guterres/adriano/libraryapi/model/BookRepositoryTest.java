package com.guterres.adriano.libraryapi.model;


import com.guterres.adriano.libraryapi.api.dto.BookDTO;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;


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

    @Test
    @DisplayName("Deve retornar um livro por id")
    public void findByIdTest(){

        Book book = getBook();

        testEntityManager.persist(book);

        Optional<Book> foundBook = iBookRepository.findById(book.getId());

        assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest(){

        Book book = getBook();
        testEntityManager.persist(book);

        Book foundBook = testEntityManager.find(Book.class, book.getId());

        iBookRepository.delete(foundBook);

        assertThat(testEntityManager.find(Book.class, book.getId())).isNull();
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){

        Book book = getBook();

        Book savedBook = iBookRepository.save(book);

        assertThat(savedBook.getId()).isGreaterThan(0);
    }




    private static Book getBook() {
        Book book = new Book();
        book.setAuthor("Author");
        book.setTitle("Title");
        book.setIsbn("123456");
        book.setId(0);
        return book;
    }
}
