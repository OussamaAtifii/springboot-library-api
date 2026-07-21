package com.atifi.library.book.repository;

import com.atifi.library.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
    boolean existsByIsbn(String isbn);
}
