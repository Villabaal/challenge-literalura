package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.models.Author;
import com.aluracursos.literalura.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author,Long> {
    Optional<Author> findByNameContainsIgnoreCase(String name);

    @Query("SELECT b FROM Author a JOIN a.books b WHERE b.title ILIKE %:title%")
    List<Book> booksByTitle( String title );

    @Query("SELECT b FROM Author a JOIN a.books b WHERE b.title ILIKE %:title% AND b.language ILIKE %:language%")
    List<Book> booksByTitleAndLanguage(String title,String language);

    @Query("SELECT b FROM Author a JOIN a.books b")
    List<Book> allBooks();

    @Query("SELECT b FROM Author a JOIN a.books b WHERE b.language ILIKE %:language%")
    List<Book> findByLanguage(String language);

    @Query("SELECT a FROM Author a WHERE a.birthYear <= :year AND :year <= a.deathYear")
    List<Author> aliveAtYear( Long year );
}
