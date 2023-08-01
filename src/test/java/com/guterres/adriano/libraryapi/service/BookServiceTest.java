package com.guterres.adriano.libraryapi.service;


import com.guterres.adriano.libraryapi.exception.BusinessException;
import com.guterres.adriano.libraryapi.model.entities.Book;
import com.guterres.adriano.libraryapi.model.repositories.IBookRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

        Mockito.when(iBookRepository.save(book))
                .thenReturn(Book.builder().id(0).isbn("123").author("fulano").title("As Aventuras").build());

        Mockito.when(iBookRepository.existsByIsbn(Mockito.anyString()))
                .thenReturn(false);

        Book savedBook = iBookService.save(book);

        System.out.println(savedBook);
        System.out.println(book);


        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo(book.getIsbn());
        assertThat(savedBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(savedBook.getAuthor()).isEqualTo(book.getAuthor());

    }

    @Test
    @DisplayName("Deve lançar um erro de negocio ao tentar salvar um livro com o isbn duplicado")
    public void shouldNotSaveABookWithDuplicatedIsbn() throws Exception{

        Book book = getBook();

        Mockito.when(iBookRepository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(()-> iBookService.save(book));

        AssertionsForClassTypes.assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("ISBN já cadastrado!");

        Mockito.verify(iBookRepository, Mockito.never()).save(book);


    }


    private static Book getBook() {
        return Book.builder().author("Author").title("Title").isbn("123456").id(10).build();
    }
}
