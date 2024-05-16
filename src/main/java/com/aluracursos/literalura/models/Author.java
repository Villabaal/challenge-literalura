package com.aluracursos.literalura.models;

import com.aluracursos.literalura.dataMappings.AuthorsData;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long birthYear;
    private Long deathYear;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Book> books = new ArrayList<>();

    public Author(){}
    public Author(AuthorsData data){
        this.birthYear = data.birthYear();
        this.deathYear = data.deathYear();
        this.name = data.name();
    }

    @Override
    public String toString() { return name; }

    public Long getBirthYear() {
        return birthYear;
    }

    public Optional<Long> getDeathYear() { return Optional.of( deathYear ); }

    public String getName() { return name; }

    public List<Book> getBooks() { return books; }

    public void addBook(Book book) {
        book.setAuthor(this);
        this.books.add(book);
    }

}
