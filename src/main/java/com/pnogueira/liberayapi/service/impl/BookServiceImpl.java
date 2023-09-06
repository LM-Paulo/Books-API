package com.pnogueira.liberayapi.service.impl;

import com.pnogueira.liberayapi.model.entity.BookEntity;
import com.pnogueira.liberayapi.model.repository.BookRepository;
import com.pnogueira.liberayapi.service.Bookservice;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl extends Bookservice {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }
    @Override
    public BookEntity save(BookEntity book) {
        return repository.save(book);
    }
}
