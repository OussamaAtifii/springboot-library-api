package com.atifi.library.book.mapper;

import com.atifi.library.author.mapper.AuthorMapper;
import com.atifi.library.author.model.Author;
import com.atifi.library.book.dto.request.CreateBookRequest;
import com.atifi.library.book.dto.request.UpdateBookRequest;
import com.atifi.library.book.dto.response.BookResponse;
import com.atifi.library.book.model.Book;

public class BookMapper {

    public static BookResponse toResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .publishedDate(book.getPublishedDate())
                .author(AuthorMapper.toResponse(book.getAuthor()))
                .build();
    }

    public static Book toEntity(CreateBookRequest request, Author author) {
        return Book.builder()
                .title(request.title())
                .isbn(request.isbn())
                .price(request.price())
                .publishedDate(request.publishedDate())
                .author(author)
                .build();
    }

    public static void updateEntity(Book book, UpdateBookRequest request, Author author) {
        book.setTitle(request.title());
        book.setIsbn(request.isbn());
        book.setPrice(request.price());
        book.setPublishedDate(request.publishedDate());
        book.setAuthor(author);
    }
}
