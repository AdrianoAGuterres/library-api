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





    private static BookDTO getBookDTO() {
        BookDTO bookDTO = BookDTO.builder().author("Author").title("Title").isbn("123456").build();
        return bookDTO;
    }

    private static Book getBook() {
        return Book.builder().author("Author").title("Title").isbn("123456").id(10).build();
    }
}
