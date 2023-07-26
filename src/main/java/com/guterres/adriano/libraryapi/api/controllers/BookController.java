package com.guterres.adriano.libraryapi.api.controllers;


import com.guterres.adriano.libraryapi.api.models.dtos.BookDTO;
import com.guterres.adriano.libraryapi.api.models.entities.Book;
import com.guterres.adriano.libraryapi.api.services.IBookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private IBookService bookService;

    private ModelMapper modelMapper;

    public BookController(IBookService bookService, ModelMapper modelMapper)
    {
        this.bookService = bookService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(BookDTO bookDTO){
        return modelMapper.map(bookService.save(modelMapper.map(bookDTO, Book.class)), BookDTO.class);
    }
}
