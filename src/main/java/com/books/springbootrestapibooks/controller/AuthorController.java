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
public class AuthorController {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;

    @GetMapping("/authors")
    public ResponseEntity<List<Author>> getAuthorRepository() {
        try {
            List<Author> authors = authorRepository.findAll();

            if (authors.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(authors, HttpStatus.OK);

        } catch (Exception e) {

            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/authors/{id}")
    public ResponseEntity<Optional<Author>> getAuthorById(@PathVariable("id") Integer id) {
        try {
            Optional<Author> author = authorRepository.findById(id);

            if (author.isPresent()) {
                return new ResponseEntity(author.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {

            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/authors/books/{id}")
    public ResponseEntity<Optional<Author>> getAuthorByBookId(@PathVariable("id") Integer id) {
        try {
            Optional<Author> author = authorRepository.findByBookId(id);

            if (author.isPresent()) {
                return new ResponseEntity(author.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {

            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/authors")
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        try {
            Optional<Author> OptionalAuthor = authorRepository.findById(author.getId());

            if (OptionalAuthor.isPresent()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            Author _author = new Author(author.getId(), author.getFirstName(),author.getLastName(), null);

            List<Book> books = new ArrayList<Book>();
            List<Book> _books = author.getBook();

            for (Book book: _books) {
                Optional<Book> OptionalBook = bookRepository.findById(book.getId());
                if (OptionalBook.isPresent()){
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
                if (book.getGenre() == null) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                books.add(new Book(book.getId(), book.getTitle(), book.getGenre(), _author));
            }
            _author.setBook(books);
            Author authorCreated = authorRepository.save(_author);
                return new ResponseEntity<>(authorCreated, HttpStatus.CREATED);
            }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/authors")
    public ResponseEntity<HttpStatus> deleteAllAuthors(){
        try {
            authorRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/authors/{id}")
    public ResponseEntity<HttpStatus> deleteAuthorById(@PathVariable("id") Integer id){
        try {
            Optional<Author> author = authorRepository.findById(id);
            if(author.isPresent()) {
                authorRepository.deleteById(id);
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
