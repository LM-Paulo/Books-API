package com.pnogueira.liberayapi.service.impl;

import com.pnogueira.liberayapi.api.exception.BusinessException;
import com.pnogueira.liberayapi.model.entity.BookEntity;
import com.pnogueira.liberayapi.model.repository.BookRepository;
import com.pnogueira.liberayapi.service.Bookservice;
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
        return Optional.empty();
    }

}
