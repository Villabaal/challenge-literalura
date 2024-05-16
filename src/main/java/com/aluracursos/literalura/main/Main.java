package com.aluracursos.literalura.main;

import com.aluracursos.literalura.dataMappings.BookData;
import com.aluracursos.literalura.dataMappings.ResultsData;
import com.aluracursos.literalura.models.Author;
import com.aluracursos.literalura.models.Book;
import com.aluracursos.literalura.repository.AuthorRepository;
import com.aluracursos.literalura.service.apiConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private final Scanner stdin = new Scanner(System.in);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String URL_BASE = "https://gutendex.com/books/";
    private Boolean terminate = false;
    private AuthorRepository repo;

    public Main(AuthorRepository repo) { this.repo=repo; }

    private void showMenu(){
        var menu = """
                    1 - Buscar libros (por autor o por titulo) ( por API )
                    2 - Buscar libros por titulo ( En DB )
                    3 - Mostrar libros ( en DB )
                    4 - Buscar libro por idioma ( en DB )
                    5 - Lista de Autores ( En DB )
                    6 - Autores vivos en determinado año ( en DB )
                    7 - Buscar autor por nombre ( en DB )
                    8 - Buscar autor por año de nacimiento ( en DB )
                    9 - Top 10 libros más descargados ( en DB )
                    0 - Salir
                    """;
        System.out.println(menu);
    }

    interface Options { void call() throws IOException, InterruptedException; }

    private void exitMenu(){ terminate = true; }

    private void searchBooks() throws IOException, InterruptedException {
        System.out.println("Ingresa el titulo del libro o autor que deseas buscar:");
        var keyPhrase = stdin.nextLine().toLowerCase().replaceAll(" ","%20");
        String json = apiConsumer.obtainData( URL_BASE+"?search="+keyPhrase );
        BookData bookData = objectMapper.readValue(json, ResultsData.class).results().get(0);
        Author author = new Author( bookData.authors().get(0) );
        Optional<Author> searchedAuthor = repo.findByNameContainsIgnoreCase(author.getName());
        Book book = new Book( bookData );
        System.out.println(book);
        List<Book> searchedBooks = repo.booksByTitleAndLanguage(book.getTitle(),book.getLanguage());
        if( searchedAuthor.isEmpty() && searchedBooks.isEmpty() ) {
            author.addBook( book );
            repo.save( author );
            return;
        }
        if (searchedAuthor.isPresent() && searchedBooks.isEmpty()) {
            searchedAuthor.get().addBook(book);
            repo.save(searchedAuthor.get());
        }
    }

    private void searchBooksByTitle(){
        System.out.println("Ingresa el titulo del libro que deseas consultar:");
        var bookName = stdin.nextLine().toLowerCase();
        var books = repo.booksByTitle( bookName );
        books.forEach( System.out::println );
    }

    private void showBooks(){
        repo.allBooks().forEach(System.out::println);
    }

    private void searchBooksByLanguage(){
        System.out.println("Ingresa el idioma del libro que deseas consultar:");
        var bookLanguage = stdin.nextLine().toLowerCase();
        System.out.println( repo.findByLanguage(bookLanguage).get(0) );
    }

    private void showAuthors() {
        var authors = repo.findAll();
        System.out.println("\n");
        authors.forEach(System.out::println);
        System.out.println("\n");
    }

    private void  authorsAlive(){
        System.out.println("Ingresa el año en el que deseas buscar autores vivos:");
        Long year = Long.parseLong( stdin.nextLine() );
        var authors = repo.aliveAtYear(year);
        System.out.println("\n");
        authors.forEach(System.out::println);
        System.out.println("\n");
    }

    private final Options[] options = new Options[]{
            this::exitMenu, this::searchBooks, this::searchBooksByTitle,
            this::showBooks, this::searchBooksByLanguage, this::showAuthors,
            this::authorsAlive,
    };

    public void menu() throws IOException, InterruptedException {
        var option = -1;
        while ( !terminate ){
            showMenu();
            option = Integer.parseInt( stdin.nextLine() );
            options[option].call();
        }
        System.out.println("Byee!!");
    }
}
