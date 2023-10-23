package com.pnogueira.liberayapi.service;


import com.pnogueira.liberayapi.model.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;


public interface Bookservice {


    BookEntity save(BookEntity any);

    Optional<BookEntity> getById(Long id);

    void delete(BookEntity book);

    BookEntity update(BookEntity book);

    public Page<BookEntity> find(BookEntity filter, Pageable pageRequest);

    public Optional<BookEntity> getBookByIsbn(String isbn);
}
