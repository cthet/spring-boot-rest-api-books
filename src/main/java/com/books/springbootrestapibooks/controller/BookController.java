package com.books.springbootrestapibooks.controller;

import com.books.springbootrestapibooks.model.Author;
import com.books.springbootrestapibooks.model.Book;
import com.books.springbootrestapibooks.repository.AuthorRepository;
import com.books.springbootrestapibooks.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooks(@RequestParam (required = false) String title){
        try {
            List<Book> books = new ArrayList<Book>();

            if (title == null) {
                bookRepository.findAll().forEach(books::add);
            } else {
                bookRepository.findByTitle(title).forEach(books::add);
            }

            if (books.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(books, HttpStatus.OK);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Optional<Book>> getBookById(@PathVariable("id") Integer id){
        try {
            Optional<Book> book = bookRepository.findById(id);
            if(!book.isPresent()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity(book.get(), HttpStatus.OK);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/books/author")
    public ResponseEntity<List<Book>> getBookByAuthorName(@RequestParam (required = true) String firstName,
                                                          @RequestParam (required = true) String lastName){
        try {
            Optional<Author> author = authorRepository.findByFirstNameAndLastName(firstName, lastName);
            if(author.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                List<Book> books = bookRepository.findByAuthorId(author.get().getId());
                return new ResponseEntity(books, HttpStatus.OK);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/books/author/{id}")
    public ResponseEntity<List<Book>> getBookByAuthorId(@PathVariable("id") Integer id){
        try {
            List<Book> books = bookRepository.findByAuthorId(id);
            if(books.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity(books, HttpStatus.OK);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@RequestBody (required = true) Book book){
        try {
            Optional<Book> OptionalBook = bookRepository.findById(book.getId());

            if(OptionalBook.isPresent()){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            Author author = book.getAuthor();

            Optional<Author> OptionalAuthor = authorRepository.findById(author.getId());

            if (OptionalAuthor.isEmpty()) {
                Author _author = new Author(author.getId(), author.getFirstName(), author.getLastName(),null);
                Book _book = new Book(book.getId(), book.getTitle(), book.getGenre(), _author);

                List<Book> books = new ArrayList<>();
                books.add(_book);
                _author.setBook(books);
                authorRepository.save(_author);
                return new ResponseEntity<>(_book, HttpStatus.CREATED);
            } else {
                Book _book = new Book(book.getId(), book.getTitle(), book.getGenre(), OptionalAuthor.get());
                Book bookCreated = bookRepository.save(_book);
                return new ResponseEntity<>(bookCreated, HttpStatus.CREATED);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") Integer id, @RequestBody Book book){
        try {
            Optional<Book> bookOptional = bookRepository.findById(id);

            if (bookOptional.isPresent()) {
                Book bookExisting = bookOptional.get();
                bookExisting.setAuthor(book.getAuthor());
                bookExisting.setTitle(book.getTitle());

                return new ResponseEntity<>(bookRepository.save(bookExisting), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/books")
    public ResponseEntity<HttpStatus> deleteAllBooks(){
        try {
            bookRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<HttpStatus> deleteBookById(@PathVariable Integer id){
        try {
            Optional<Book> book = bookRepository.findById(id);
            if(book.isPresent()) {
                bookRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
