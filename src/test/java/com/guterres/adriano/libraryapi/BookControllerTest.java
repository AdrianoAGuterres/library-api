package com.guterres.adriano.libraryapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guterres.adriano.libraryapi.exception.BusinessException;
import com.guterres.adriano.libraryapi.api.dto.BookDTO;
import com.guterres.adriano.libraryapi.service.IBookService;
import com.guterres.adriano.libraryapi.model.entities.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IBookService bookService;

    @Test
    @DisplayName("Deve criar um livro com sucesso")
    public void createBookTest() throws Exception {

        BookDTO bookDTO = getBookDTO();

        Book book = getBook();
        BDDMockito.given(bookService.save(Mockito.any(Book.class))).willReturn(book);


        String jsonBody = new ObjectMapper().writeValueAsString(bookDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonBody);

        mockMvc
                .perform(request)

                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("title").value(bookDTO.getTitle()))
                .andExpect(jsonPath("author").value(bookDTO.getAuthor()))
                .andExpect(jsonPath("isbn").value(bookDTO.getIsbn()));

    }

    @Test
    @DisplayName("Deve lançar um erro quando não houver dados suficientes para a criação do livro")
    public void createInvalidBookTest() throws Exception{

        String bookDtoJson = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(bookDtoJson);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(3)));

    }

    @Test
    @DisplayName("Deve lançar um erro quando já houver livro com o isbn informado")
    public void createBookWithDuplicatedIsbn() throws Exception{

        String errorMsg = "Isbn já cadastrado!";

        String bookDtoJson = new ObjectMapper().writeValueAsString(getBookDTO());

        BDDMockito.given(bookService.save(Mockito.any())).willThrow(new BusinessException(errorMsg));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(bookDtoJson);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors").value(errorMsg));

    }

    @Test
    @DisplayName("Deve obter informações de um livro.")
    public void getBookDetailsTest() throws Exception{

        Long id = 1l;

        Book book = getBook();
        book.setId(id);

        BDDMockito.given(bookService.getById(id)).willReturn(Optional.of(book));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(book.getId()))
                .andExpect(jsonPath("title").value(book.getTitle()))
                .andExpect(jsonPath("author").value(book.getAuthor()))
                .andExpect(jsonPath("isbn").value(book.getIsbn()));
    }


    @Test
    @DisplayName("Deve retornar resource not found quand o livro não existir")
    public void bookNotFoundTest() throws Exception{

        BDDMockito.given(bookService.getById(Mockito.anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Deve deletar um livro")
    public void deleteBookTest() throws Exception{

        Book book = getBook();
        book.setId(1);

        BDDMockito.given(bookService.getById(Mockito.anyLong())).willReturn(Optional.of(book));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("Deve retornar not found quando não encontrar o livro para deletar.")
    public void deleteNonexistentBookTest() throws Exception{

        BDDMockito.given(bookService.getById(Mockito.anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + 1))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andExpect(status().isNotFound());

    }
    @Test
    @DisplayName("Deve atualizar um livro.")
    public void updateBookTest() throws Exception{

        Long id = 1l;

        Book updatingBook = getBook();
        updatingBook.setAuthor("A");
        updatingBook.setTitle("T");
        updatingBook.setId(1);

        String bookJson = new ObjectMapper().writeValueAsString(updatingBook);

        Book updatedBook = getBook();
        updatedBook.setId(1);


        BDDMockito.given(bookService.getById(id))
                .willReturn(Optional.of(updatingBook));

        BDDMockito.given(bookService.update(updatingBook))
                .willReturn(updatedBook);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" + id))
                .content(bookJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(updatedBook.getId()))
                .andExpect(jsonPath("isbn").value(updatedBook.getIsbn()))
                .andExpect(jsonPath("title").value(updatedBook.getTitle()))
                .andExpect(jsonPath("author").value(updatedBook.getAuthor()));

    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar atualizar um livro inexistente.")
    public void updateNonexistentBookTest() throws Exception{

        String bookJson = new ObjectMapper().writeValueAsString(getBook());

        BDDMockito.given(bookService.getById(Mockito.anyLong()))
                .willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" + 1))
                .content(bookJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andExpect(status().isNotFound());

    }



    private static BookDTO getBookDTO() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setAuthor("Author");
        bookDTO.setTitle("Title");
        bookDTO.setIsbn("123456");
        return bookDTO;
    }
    private static Book getBook() {
        Book book = new Book();
        book.setAuthor("Author");
        book.setTitle("Title");
        book.setIsbn("123456");
        return book;
    }
}
