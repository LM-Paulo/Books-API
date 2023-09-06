package com.pnogueira.liberayapi.api.resource;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pnogueira.liberayapi.api.dto.BookDTO;
import com.pnogueira.liberayapi.api.exception.BusinessException;
import com.pnogueira.liberayapi.model.entity.BookEntity;
import com.pnogueira.liberayapi.service.Bookservice;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Matches;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

    static String BOOK_API = "/api/books"; // define a rota da api

    @Autowired
    MockMvc mvc; // injeção para simular as requisições

    @MockBean
    Bookservice service;

    @Test
    @DisplayName("Deve criar um livro com sucesso.")
    public void createBookTest() throws Exception {


        BookDTO dto = createNewBook();

        BookEntity savedBook = BookEntity.builder().id(101L).author("PAULO").title("As aventuras").isbn("001").build();


        BDDMockito.given((service.save(Mockito.any(BookEntity.class)))).willReturn(savedBook);

        String json = new ObjectMapper().writeValueAsString(dto); //recebe um objeto e o transforma em json

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        mvc
                .perform(request)
                .andExpect(status().isCreated() )
                .andExpect(jsonPath("id").isNotEmpty() )
                .andExpect(jsonPath("title").value(dto.getTitle()) )
                .andExpect(jsonPath("author").value(dto.getAuthor()) )
                .andExpect(jsonPath("isbn").value(dto.getIsbn()) )
        ;
    }


    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficientes.")
    public void createInvalidBookTest() throws Exception{

        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors",Matchers.hasSize(3)));
    }


    @Test
    @DisplayName("Deve lançar error ao tentar cadastrar isbn repetida")
    public void createBookWithDuplicateIsbn() throws Exception {

        BookDTO dto = createNewBook();

        String json = new ObjectMapper().writeValueAsString(dto);
        String mensagemErro = "Isbn já cadastrada";

        BDDMockito.given(service.save(Mockito.any(BookEntity.class)))
                .willThrow(new BusinessException(mensagemErro));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors",Matchers.hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(mensagemErro));

    }

    private static BookDTO createNewBook() {
        return BookDTO.builder().author("PAULO").title("As aventuras").isbn("001").build();
    }
}
