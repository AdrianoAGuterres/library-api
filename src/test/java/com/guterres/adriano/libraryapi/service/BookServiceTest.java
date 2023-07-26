package com.guterres.adriano.libraryapi.service;


import com.guterres.adriano.libraryapi.api.models.entities.Book;
import com.guterres.adriano.libraryapi.api.models.repositories.IBookRepository;
import com.guterres.adriano.libraryapi.api.services.BookService;
import com.guterres.adriano.libraryapi.api.services.IBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.json.GsonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.SerializationUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class BookServiceTest {

    IBookService iBookService;

    @MockBean
    IBookRepository iBookRepository;

    @BeforeEach
    public void setUp(){
        this.iBookService = new BookService(iBookRepository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){

        Book book = Book.builder().id(0).isbn("123").author("fulano").title("As Aventuras").build();

        Mockito.when(iBookRepository.save(book)).thenReturn(Book.builder().id(0).isbn("123").author("fulano").title("As Aventuras").build());

        Book savedBook = iBookService.save(book);

        System.out.println(savedBook);
        System.out.println(book);


        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo(book.getIsbn());
        assertThat(savedBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(savedBook.getAuthor()).isEqualTo(book.getAuthor());

    }
}
