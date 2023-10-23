package com.pnogueira.liberayapi.api.resource;

import com.pnogueira.liberayapi.api.dto.BookDTO;
import com.pnogueira.liberayapi.api.exception.ApiErros;
import com.pnogueira.liberayapi.api.exception.BusinessException;
import com.pnogueira.liberayapi.model.entity.BookEntity;
import com.pnogueira.liberayapi.service.Bookservice;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@Slf4j
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


    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        BookEntity book = service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        service.delete(book);


    }

    @PutMapping("{id}")
    public BookDTO update( @PathVariable Long id, @RequestBody @Valid BookDTO dto){
        log.info(" updating book of id: {} ", id);
        return service.getById(id).map( book -> {

            book.setAuthor(dto.getAuthor());
            book.setTitle(dto.getTitle());
            book = service.update(book);
            return modelMapper.map(book, BookDTO.class);

        }).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
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

    @GetMapping
    @ApiOperation("Find books by params")
    public Page<BookDTO> find(BookDTO dto, Pageable pageRequest){
        BookEntity filter = modelMapper.map(dto,BookEntity.class);
        Page<BookEntity> result = service.find(filter,pageRequest);
        List<BookDTO> list = result.getContent()
                .stream()
                .map(bookEntity -> modelMapper.map(bookEntity,BookDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<BookDTO>(list,pageRequest,result.getTotalElements());
    }

    
}
