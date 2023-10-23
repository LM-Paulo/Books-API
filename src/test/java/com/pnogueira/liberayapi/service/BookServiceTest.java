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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


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

    @Test
    @DisplayName("deve obter um livro por id")
    public void getByidTest(){
        //cenario
        Long id = 11l;
        BookEntity book = createValidBook();
        book.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

        //execução
        Optional<BookEntity> foudBook = service.getById(id);

        //verificação
        assertThat( foudBook.isPresent() ).isTrue();
        assertThat( foudBook.get().getId() ).isEqualTo(id);
        assertThat( foudBook.get().getAuthor() ).isEqualTo(book.getAuthor());
        assertThat( foudBook.get().getTitle() ).isEqualTo(book.getTitle());
        assertThat( foudBook.get().getIsbn() ).isEqualTo(book.getIsbn());


    }

    @Test
    @DisplayName("deve retornar not faud quando nao encontrar id cadastrado")
    public void bookNotFoudByIdTest(){
        //cenario
        Long id = 11l;

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //execução
        Optional<BookEntity> Book = service.getById(id);

        //verificação
        assertThat(Book.isPresent() ).isFalse();

    }

    @Test
    @DisplayName("Deve deletar um livro.")
    public void deleteBookTest() {
        BookEntity book = BookEntity.builder().id(11L).title("TESTE").isbn("123456").author("PAULO").build();

        // execucao
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> service.delete(book));

        // verificacao
        Mockito.verify(repository, Mockito.times(1)).delete(book);
    }
    @Test
    @DisplayName("Deve ocorrer um erro ao tentar deletar um livro inexistente.")
    public void deleteInvalidBookTest() {
        BookEntity book = new BookEntity();

        // execucao
        assertThrows(IllegalArgumentException.class, () -> service.delete(book));

        // verificacao
        Mockito.verify(repository, Mockito.never()).delete(book);
    }


    @Test
    @DisplayName("Deve atualizar um livro.")
    public void updateBookTest() {
        Long id = 1l;
        // livro a atualizar
        BookEntity updating = BookEntity.builder().id(id).build();

        // simulacao
        BookEntity updatedBook = createValidBook();
        updatedBook.setId(id);

        Mockito.when(repository.save(updating)).thenReturn(updatedBook);

        // execucao
        BookEntity book = service.update(updating);

        // verificacao
        assertThat(book.getId()).isEqualTo(updatedBook.getId());
        assertThat(book.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(book.getIsbn()).isEqualTo(updatedBook.getIsbn());
        assertThat(book.getAuthor()).isEqualTo(updatedBook.getAuthor());
    }


    @Test
    @DisplayName("Deve ocorrer um erro ao tentar atualizar um livro inexistente.")
    public void updateInvalidBookTest() {
        BookEntity book = new BookEntity();

        // execucao
        assertThrows(IllegalArgumentException.class,
                () -> service.delete(book));

        // verificacao
        Mockito.verify(repository, Mockito.never()).save(book);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Deve filtrar livros pelas propriedades.")
    public void findBookTest() {
        // cenario
        BookEntity book = createValidBook();

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<BookEntity> lista = Arrays.asList(book);
        Page<BookEntity> page = new PageImpl<BookEntity>(lista, pageRequest, 1);

        when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        // execucao
        Page<BookEntity> result = service.find(book, pageRequest);

        // verificacoes
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(lista);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Deve obter um livro pelo isbn.")
    public void getBookByIsbnTest() {
        String isbn = "1230";
        when(repository.findByIsbn(isbn)).thenReturn(Optional.of(BookEntity.builder().id(1l).isbn(isbn).build()));

        Optional<BookEntity> book = service.getBookByIsbn(isbn);

        assertThat(book.isPresent()).isTrue();
        assertThat(book.get().getId()).isEqualTo(1l);
        assertThat(book.get().getIsbn()).isEqualTo(isbn);

        Mockito.verify(repository, Mockito.times(1)).findByIsbn(isbn);
    }

}
