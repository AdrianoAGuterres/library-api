package com.guterres.adriano.libraryapi.service;

import com.guterres.adriano.libraryapi.exception.BusinessException;
import com.guterres.adriano.libraryapi.model.repositories.IBookRepository;
import com.guterres.adriano.libraryapi.model.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class BookService implements IBookService {

    private IBookRepository iBookRepository;

    public BookService(IBookRepository repository) {
        this.iBookRepository = repository;
    }

    @Override
    public Book save(Book book) {
        if( iBookRepository.existsByIsbn(book.getIsbn()) ){
            throw new BusinessException("ISBN já cadastrado!");
        }
        return iBookRepository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return this.iBookRepository.findById(id);
    }

    @Override
    public void delete(Book book) {
        if (Objects.isNull(book)){
            throw new NullPointerException("O Livro não pode ser nulo!");
        } else if (book.getId() == 0){
            throw new IllegalArgumentException("O id não do Livro não pode ser menor que 1!");
        }
        this.iBookRepository.delete(book);
    }

    @Override
    public Book update(Book book) {
        if (Objects.isNull(book)){
            throw new NullPointerException("O Livro não pode ser nulo!");
        } else if (book.getId() == 0){
            throw new IllegalArgumentException("O id não do Livro não pode ser menor que 1!");
        }
        return this.iBookRepository.save(book);
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
