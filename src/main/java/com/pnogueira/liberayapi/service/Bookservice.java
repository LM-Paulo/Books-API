package com.pnogueira.liberayapi.service;

import com.pnogueira.liberayapi.model.entity.BookEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface Bookservice {


    BookEntity save(BookEntity any);

    Optional<BookEntity> getById(Long id);
}
