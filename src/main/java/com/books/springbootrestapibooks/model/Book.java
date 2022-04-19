package com.books.springbootrestapibooks.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private int id;

    @NotBlank
    @Column(name = "title")
    private String title;

    @NotBlank
    @NotNull
    @Column(name = "genre_id")
    private EGenre genre;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @OnDelete(action =  OnDeleteAction.CASCADE)
    private Author author;

    public Book(int id, String title){
        this.id = id;
        this.title = title;
    }

    public Book(int id, String title, Author author, EGenre genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
    }


}
