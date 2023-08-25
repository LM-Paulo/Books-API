package com.pnogueira.liberayapi.api.resource;

import com.pnogueira.liberayapi.api.dto.BookDTO;
import com.pnogueira.liberayapi.model.entity.BookEntity;
import com.pnogueira.liberayapi.service.Bookservice;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {



    private Bookservice service;
    private ModelMapper modelMapper;

    public BookController(Bookservice service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;

    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody BookDTO dto){

       BookEntity entity = modelMapper.map(dto,BookEntity.class);
       entity = service.save(entity);
       return modelMapper.map(entity, BookDTO.class);

    }
}
