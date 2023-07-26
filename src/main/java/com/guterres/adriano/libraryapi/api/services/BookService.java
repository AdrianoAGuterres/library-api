package com.guterres.adriano.libraryapi.api.services;

import com.guterres.adriano.libraryapi.api.models.repositories.IBookRepository;
import com.guterres.adriano.libraryapi.api.models.entities.Book;
import org.springframework.stereotype.Service;

@Service
public class BookService implements IBookService {

    private IBookRepository iBookRepository;

    public BookService(IBookRepository iBookRepository){
        this.iBookRepository = iBookRepository;
    }

    @Override
    public Book save(Book book) {
        return iBookRepository.save(book);
    }
}
