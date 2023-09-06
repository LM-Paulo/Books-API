package com.pnogueira.liberayapi.service;

import com.pnogueira.liberayapi.model.entity.BookEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class Bookservice {


    public BookEntity save(BookEntity any) {
        return any;
    }

}
