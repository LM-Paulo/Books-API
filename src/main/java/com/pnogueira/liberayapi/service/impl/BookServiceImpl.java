package com.pnogueira.liberayapi.service.impl;

import com.pnogueira.liberayapi.api.exception.BusinessException;
import com.pnogueira.liberayapi.model.entity.BookEntity;
import com.pnogueira.liberayapi.model.repository.BookRepository;
import com.pnogueira.liberayapi.service.Bookservice;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements Bookservice {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }
    @Override
    public BookEntity save(BookEntity book) {
        if (repository.existsByIsbn(book.getIsbn())){
            throw new BusinessException("Isbn ja cadastrada");
        }

        return repository.save(book);
    }

    @Override
    public Optional<BookEntity> getById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public void delete(BookEntity book) {
        if(book == null || book.getId() == null)
            throw new IllegalArgumentException("Book id cant be null.");

        this.repository.delete(book);
    }

    @Override
    public BookEntity update(BookEntity book) {
        if(book == null || book.getId() == null){
            throw  new IllegalArgumentException("Book id cant be null.");
        }

        return this.repository.save(book);

    }

    @Override
    public Page<BookEntity> find(BookEntity filter, Pageable pageRequest) {
        Example<BookEntity> example = Example.of(filter,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return repository.findAll(example, pageRequest);
    }

    @Override
    public Optional<BookEntity> getBookByIsbn(String isbn) {
        return repository.findByIsbn(isbn);
    }


}
