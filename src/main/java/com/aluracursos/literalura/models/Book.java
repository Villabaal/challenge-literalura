package com.aluracursos.literalura.models;

import com.aluracursos.literalura.dataMappings.BookData;
import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;


@Entity
@Table(name = "books",
        uniqueConstraints={ @UniqueConstraint(columnNames = {"title", "language","author_id"})})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne
    private Author author;
    private List< String > subjects;
    private Long downloadCount;
    private String language;

    public Book(){}
    public Book( BookData data ){
        this.title = data.title();
        this.subjects = data.subjects();
        this.downloadCount = data.downloadCount();
        this.language = data.languages().get(0);
    }

    @Override
    public String toString() {
        return  '\n' +"title: " + title + '\n' +
                "author: " + author + '\n' +
                "subjects: " + subjects + '\n' +
                "language: " + language + '\n' +
                "downloadCount: " + downloadCount + '\n'+'\n';
    }

    public String getTitle() { return title; }

    public Optional<Author> getAuthor() { return Optional.of(author); }

    public void setAuthor(Author author) { this.author = author; }

    public List<String> getSubjects() { return subjects; }

    public Long getDownloadCount() { return downloadCount; }

    public String getLanguage() { return language; }
}
