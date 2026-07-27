package com.atifi.library.book.controller;

import com.atifi.library.book.dto.request.BookFilter;
import com.atifi.library.book.dto.request.CreateBookRequest;
import com.atifi.library.book.dto.request.UpdateBookRequest;
import com.atifi.library.book.dto.response.BookResponse;
import com.atifi.library.book.service.BookService;
import com.atifi.library.constants.ApiConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.BOOKS_BASE)
@RequiredArgsConstructor
public class BookController {
    private final BookService service;

    @GetMapping
    public ResponseEntity<Page<BookResponse>> findAll(@ModelAttribute BookFilter filters, Pageable pageable) {
        Page<BookResponse> books = service.findAll(filters, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @GetMapping(ApiConstants.PATH_ID)
    public ResponseEntity<BookResponse> findById(@PathVariable Integer id) {
        BookResponse bookResponse = service.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(bookResponse);
    }

    @PostMapping
    public ResponseEntity<BookResponse> save(@Valid @RequestBody CreateBookRequest request) {
        BookResponse bookResponse = service.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookResponse);
    }

    @PutMapping(ApiConstants.PATH_ID)
    public ResponseEntity<BookResponse> update(@PathVariable Integer id, @RequestBody UpdateBookRequest request) {
        BookResponse bookResponse = service.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(bookResponse);
    }

    @DeleteMapping(ApiConstants.PATH_ID)
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
