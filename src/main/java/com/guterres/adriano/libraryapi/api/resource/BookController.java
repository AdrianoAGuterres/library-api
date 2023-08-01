package com.guterres.adriano.libraryapi.api.resource;


import com.guterres.adriano.libraryapi.api.exceptions.ApiErrors;
import com.guterres.adriano.libraryapi.exception.BusinessException;
import com.guterres.adriano.libraryapi.api.dto.BookDTO;
import com.guterres.adriano.libraryapi.model.entities.Book;
import com.guterres.adriano.libraryapi.service.IBookService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

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
    public BookDTO create(@RequestBody @Valid BookDTO bookDTO){
        return modelMapper.map(bookService.save(modelMapper.map(bookDTO, Book.class)), BookDTO.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationException(MethodArgumentNotValidException exception){
        return new ApiErrors(exception.getBindingResult());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleBusinessException(BusinessException exception){
        return new ApiErrors(exception);
    }
}
