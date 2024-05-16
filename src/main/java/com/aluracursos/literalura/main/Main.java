package com.aluracursos.literalura.main;

import com.aluracursos.literalura.dataMappings.AuthorsData;
import com.aluracursos.literalura.dataMappings.BookData;
import com.aluracursos.literalura.dataMappings.ResultsData;
import com.aluracursos.literalura.models.Author;
import com.aluracursos.literalura.models.Book;
import com.aluracursos.literalura.repository.AuthorRepository;
import com.aluracursos.literalura.service.apiConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
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
                    7 - Número de libros en determinado idioma ( en DB )
                    8 - Buscar autor por nombre ( en DB )
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
        var results = objectMapper.readValue(json, ResultsData.class).results();
        if ( results.isEmpty() ){ System.out.println("\n\nNo se encontró\n"); return; }
        BookData bookData = results.get(0);
        AuthorsData authorData = bookData.authors().get(0);
        Optional<Author> searchedAuthor = repo.findByNameContainsIgnoreCase( authorData.name() );
        //el autor existe en DB
        if ( searchedAuthor.isPresent() ){
            Optional<Book> searchedBook = searchedAuthor.get().getBooks().stream()
                    .filter( b -> b.getTitle().equalsIgnoreCase(bookData.title()) &&
                            b.getLanguage().equalsIgnoreCase(bookData.languages().get(0)) )
                    .findFirst();
            //el libro ya esta en DB
            if (searchedBook.isPresent()){
                System.out.println( "\n\n"+searchedBook.get()+"\n" );
                return;
            }
            //el libro no esta en DB
            Book book = new Book( bookData );
            searchedAuthor.get().addBook( book );
            System.out.println( "\n\n"+book+"\n" );
            repo.save( searchedAuthor.get() );
            return;
        }
        //el autor no existe en db
        Author author = new Author( authorData );
        Book book = new Book( bookData );
        author.addBook( book );
        repo.save( author );
        System.out.println( "\n\n"+book+"\n" );
    }

    private void searchBooksByTitle(){
        System.out.println("Ingresa el titulo del libro que deseas consultar:");
        var bookName = stdin.nextLine().toLowerCase();
        var books = repo.booksByTitle( bookName );
        if ( books.isEmpty() ){ System.out.println("\n\nNo se encontró\n"); return; }
        books.forEach( System.out::println );
    }

    private void showBooks(){
        repo.allBooks().forEach(System.out::println);
    }

    private void searchBookByLanguage(){
        System.out.println("Ingresa el idioma del libro que deseas consultar:");
        var bookLanguage = stdin.nextLine().toLowerCase();
        var books = repo.findByLanguage(bookLanguage);
        if ( books.isEmpty() ){ System.out.println("\n\nNo se encontró\n"); return; }
        System.out.println( books.get(0) );
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

    private void countBooksByLanguage(){
        System.out.println("Cuantos libros hay del idioma?:");
        var bookLanguage = stdin.nextLine().toLowerCase();
        var bookCount = repo.findByLanguage(bookLanguage).size();
        var msg = "\n\nCantidad de libros del idioma %s: %d\n\n"
                .formatted(bookLanguage,bookCount);
        System.out.println( msg );
    }

    private void searchAuthorByName() {
        System.out.println("Ingresa el nombre del autor que deseas consultar:");
        var authorName = stdin.nextLine();
        Optional<Author> searchedAuthor = repo.findByNameContainsIgnoreCase( authorName );
        if ( searchedAuthor.isPresent() ) {
            System.out.println("\n\n" + searchedAuthor.get() + "\n");
            return;
        }
        System.out.println("\n\nNo se encontró\n");
    }

    private void top10Books(){
        var books = repo.top10Books();
        System.out.println("\n");
        books.forEach( System.out::println );
        System.out.println("\n");
    }

        private final Options[] options = new Options[]{
            this::exitMenu, this::searchBooks, this::searchBooksByTitle,
            this::showBooks, this::searchBookByLanguage, this::showAuthors,
            this::authorsAlive, this::countBooksByLanguage,this::searchAuthorByName,
            this::top10Books
    };

    public void menu() throws IOException, InterruptedException {
        var option = -1;
        while ( !terminate ){
            showMenu();
            try{
                option = Integer.parseInt( stdin.nextLine() );
                options[option].call();
            }catch (NumberFormatException | IndexOutOfBoundsException e){
                System.out.println("\n\nIntroduce un valor válido\n");
            }
        }
        System.out.println("Byee!!");
    }
}
