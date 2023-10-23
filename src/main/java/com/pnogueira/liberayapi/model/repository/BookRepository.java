package com.pnogueira.liberayapi.model.repository;

import com.pnogueira.liberayapi.model.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity,Long> {
    boolean existsByIsbn(String isbn);

    Optional<BookEntity> findByIsbn(String isbn);
}
