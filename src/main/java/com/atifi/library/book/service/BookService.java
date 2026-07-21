package com.atifi.library.book.service;

import com.atifi.library.author.exception.AuthorNotFoundException;
import com.atifi.library.author.model.Author;
import com.atifi.library.author.repository.AuthorRepository;
import com.atifi.library.book.dto.request.CreateBookRequest;
import com.atifi.library.book.dto.request.UpdateBookRequest;
import com.atifi.library.book.dto.response.BookResponse;
import com.atifi.library.book.exception.BookAlreadyExistsException;
import com.atifi.library.book.exception.BookNotFoundException;
import com.atifi.library.book.mapper.BookMapper;
import com.atifi.library.book.model.Book;
import com.atifi.library.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public List<BookResponse> findAll() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(BookMapper::toResponse).toList();
    }

    public BookResponse findById(Integer id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        return BookMapper.toResponse(book);
    }

    public BookResponse save(CreateBookRequest request) {
        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new BookAlreadyExistsException(request.isbn());
        }

        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new AuthorNotFoundException(request.authorId()));

        Book book = BookMapper.toEntity(request, author);
        Book createdBook = bookRepository.save(book);

        return BookMapper.toResponse(createdBook);
    }

    public BookResponse update(Integer id, UpdateBookRequest request) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        if (!book.getIsbn().equals(request.isbn())
                && bookRepository.existsByIsbn(request.isbn())) {
            throw new BookAlreadyExistsException(request.isbn());
        }

        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new AuthorNotFoundException(request.authorId()));

        BookMapper.updateEntity(book, request, author);

        Book updatedBook = bookRepository.save(book);

        return BookMapper.toResponse(updatedBook);
    }

    public void deleteById(Integer id) {
        bookRepository.deleteById(id);
    }
}
