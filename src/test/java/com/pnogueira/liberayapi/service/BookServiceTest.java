package com.pnogueira.liberayapi.service;

import com.pnogueira.liberayapi.api.exception.BusinessException;
import com.pnogueira.liberayapi.model.entity.BookEntity;
import com.pnogueira.liberayapi.model.repository.BookRepository;
import com.pnogueira.liberayapi.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    Bookservice service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){

        //cenario
        BookEntity book = createValidBook();

        Mockito.when(repository.save(book))
                .thenReturn(BookEntity.builder()
                        .id(11L)
                        .isbn("123")
                        .title("As aventuras")
                        .author("Paulo")
                        .build());

        //Execução
        BookEntity savedBook = service.save(book);

       //verificação
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getAuthor()).isEqualTo("Paulo");
        assertThat(savedBook.getTitle()).isEqualTo("As aventuras");

    }

    private static BookEntity createValidBook() {
        return BookEntity.builder().isbn("123").author("Paulo").title("As aventuras").build();
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com isbn duplicado")
    public void shouldNotSaveABookWithDuplicateISBN(){
        //cenario
        BookEntity book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        //execucao
        Throwable exception = Assertions.catchThrowable(() -> service.save(book));

        //verificacao
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn ja cadastrada");


        Mockito.verify(repository, Mockito.never()).save(book);
    }


}
