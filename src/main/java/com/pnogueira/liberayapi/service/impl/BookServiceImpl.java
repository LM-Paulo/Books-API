package com.pnogueira.liberayapi.service.impl;

import com.pnogueira.liberayapi.api.exception.BusinessException;
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
        if (repository.existsByIsbn(book.getIsbn())){
            throw new BusinessException("Isbn ja cadastrada");
        }
        return repository.save(book);
    }
}
