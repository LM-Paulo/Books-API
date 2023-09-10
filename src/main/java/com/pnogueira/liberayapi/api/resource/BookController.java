package com.pnogueira.liberayapi.api.resource;

import com.pnogueira.liberayapi.api.dto.BookDTO;
import com.pnogueira.liberayapi.api.exception.ApiErros;
import com.pnogueira.liberayapi.api.exception.BusinessException;
import com.pnogueira.liberayapi.model.entity.BookEntity;
import com.pnogueira.liberayapi.service.Bookservice;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    public BookDTO create(@RequestBody  @Valid BookDTO dto){

       BookEntity entity = modelMapper.map(dto,BookEntity.class);
       entity = service.save(entity);
       return modelMapper.map(entity, BookDTO.class);

    }
    @GetMapping("{id}")
    public BookDTO get(@PathVariable Long id){
        return service
                .getById(id)
                .map(bookEntity -> modelMapper.map(bookEntity,BookDTO.class) )
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));



    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handlerValidationException(MethodArgumentNotValidException ex){
      BindingResult bindingResult = ex.getBindingResult();
      return  new ApiErros(bindingResult);

    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handlerBusinessException(BusinessException ex){
        return new ApiErros(ex);

    }

    
}
