package com.books.springbootrestapibooks.repository;

import com.books.springbootrestapibooks.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Optional<Author> findById(Integer id);
    Optional<Author> findByBookId(Integer id);
    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
