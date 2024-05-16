package com.aluracursos.literalura.models;

import com.aluracursos.literalura.dataMappings.BookData;
import jakarta.persistence.*;

import java.util.List;


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

    public void setAuthor(Author author) { this.author = author; }

    public String getLanguage() { return language; }

    public void setDownloadCount(Long downloadCount) { this.downloadCount = downloadCount; }
}
