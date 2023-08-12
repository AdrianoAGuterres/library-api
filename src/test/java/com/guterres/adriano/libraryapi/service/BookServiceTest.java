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

import java.util.Optional;

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

        Book book = getBook();
        book.setId(0);

        Mockito.when(iBookRepository.save(book))
                .thenReturn(getBook());

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

    @Test
    @DisplayName("Deve obter um livro por id")
    public void getByIdTest() throws Exception{

        Book baseBook = getBook();
        Long id = baseBook.getId();

        Mockito.when(iBookRepository.findById(id)).thenReturn(Optional.of(baseBook));

        Optional<Book> foundBook = iBookService.getById(id);

        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(baseBook.getId());
        assertThat(foundBook.get().getIsbn()).isEqualTo(baseBook.getIsbn());
        assertThat(foundBook.get().getTitle()).isEqualTo(baseBook.getTitle());
        assertThat(foundBook.get().getAuthor()).isEqualTo(baseBook.getAuthor());
    }
    @Test
    @DisplayName("Deve retornar vazio quando o livro for inexistente")
    public void bookNotFoundByIdTest() throws Exception{

        Long id = getBook().getId();

        Mockito.when(iBookRepository.findById(id)).thenReturn(Optional.empty());

        assertThat(iBookService.getById(id).isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest() throws Exception{

        Book book = getBook();

        Assertions.assertThatCode(() -> {
                    iBookService.delete(book);
                }).doesNotThrowAnyException();

        Mockito.verify(iBookRepository, Mockito.times(1)).delete(book);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar um livro com id menor que 1")
    public void deleteBookWithIdLessThanOneTest() throws Exception{

        Book book = getBook();
        book.setId(0);

        Assertions.assertThatCode(() -> {
            iBookService.delete(book);
        })
                .hasNoSuppressedExceptions()
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageEndingWith("O id não do Livro não pode ser menor que 1!");

        Mockito.verify(iBookRepository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar um livro com id mulo")
    public void deleteBookNullTest() throws Exception{

        Book book = null;
        Assertions.assertThatCode(() -> {
            iBookService.delete(book);
        })
                .hasNoSuppressedExceptions()
                .isInstanceOf(NullPointerException.class)
                .hasMessageEndingWith("O Livro não pode ser nulo!");

        Mockito.verify(iBookRepository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar um livro com id menor que 1")
    public void updateBookWithIdLessThanOneTest() throws Exception{

        Book book = getBook();
        book.setId(0);

        Assertions.assertThatCode(() -> {
                    iBookService.update(book);
                })
                .hasNoSuppressedExceptions()
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageEndingWith("O id não do Livro não pode ser menor que 1!");

        Mockito.verify(iBookRepository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar um livro com id nulo")
    public void updateBookNullTest() throws Exception{

        Book book = null;
        Assertions.assertThatCode(() -> {
                    iBookService.update(book);
                })
                .hasNoSuppressedExceptions()
                .isInstanceOf(NullPointerException.class)
                .hasMessageEndingWith("O Livro não pode ser nulo!");

        Mockito.verify(iBookRepository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateBookTest() throws Exception{

        Book book = getBook();
        Book updatedBook = getBook();

        Mockito.when(iBookRepository.save(book)).thenReturn(updatedBook);

        Book book2 = iBookService.update(updatedBook);

       assertThat(book2).isEqualTo(updatedBook);

        Mockito.verify(iBookRepository, Mockito.times(1)).save(book);
    }



    private static Book getBook() {
        Book book = new Book();
        book.setAuthor("Author");
        book.setTitle("Title");
        book.setIsbn("123456");
        book.setId(1);
        return book;
    }
}
