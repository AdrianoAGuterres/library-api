package com.guterres.adriano.libraryapi.service;

import com.guterres.adriano.libraryapi.exception.BusinessException;
import com.guterres.adriano.libraryapi.model.repositories.IBookRepository;
import com.guterres.adriano.libraryapi.model.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService implements IBookService {

    private IBookRepository repository;

    public BookService(IBookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if( repository.existsByIsbn(book.getIsbn()) ){
            throw new BusinessException("ISBN já cadastrado!");
        }
        return repository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public void delete(Book book) {

    }

    @Override
    public Book update(Book book) {
        return null;
    }

    @Override
    public Page<Book> find(Book filter, Pageable pageRequest) {
        return null;
    }

    @Override
    public Optional<Book> getBookByIsbn(String isbn) {
        return Optional.empty();
    }


}
