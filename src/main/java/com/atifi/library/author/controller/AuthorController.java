package com.atifi.library.author.controller;

import com.atifi.library.author.dto.request.AuthorFilter;
import com.atifi.library.author.dto.request.CreateAuthorRequest;
import com.atifi.library.author.dto.request.UpdateAuthorRequest;
import com.atifi.library.author.dto.response.AuthorResponse;
import com.atifi.library.author.service.AuthorService;
import com.atifi.library.constants.ApiConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

@RestController
@RequestMapping(ApiConstants.AUTHORS_BASE)
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService service;

    @GetMapping
    public ResponseEntity<List<AuthorResponse>> findAll(@ModelAttribute AuthorFilter filters) {
        List<AuthorResponse> authors = service.findAll(filters);
        return ResponseEntity.status(HttpStatus.OK).body(authors);
    }

    @GetMapping(ApiConstants.PATH_ID)
    public ResponseEntity<AuthorResponse> findById(@PathVariable Integer id) {
        AuthorResponse authorResponse = service.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(authorResponse);
    }

    @PostMapping
    public ResponseEntity<AuthorResponse> save(@Valid @RequestBody CreateAuthorRequest request) {
        AuthorResponse authorResponse = service.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(authorResponse);
    }

    @PutMapping(ApiConstants.PATH_ID)
    public ResponseEntity<AuthorResponse> update(@PathVariable Integer id, @RequestBody UpdateAuthorRequest request) {
        AuthorResponse authorResponse = service.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(authorResponse);
    }

    @DeleteMapping(ApiConstants.PATH_ID)
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
