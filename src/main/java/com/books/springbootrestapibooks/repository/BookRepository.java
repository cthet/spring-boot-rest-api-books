package com.books.springbootrestapibooks.repository;

import com.books.springbootrestapibooks.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByTitle(String title);
    List<Book> findByAuthorId(Integer id);
}
