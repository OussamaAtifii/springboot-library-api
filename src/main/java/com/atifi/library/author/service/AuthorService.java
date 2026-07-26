package com.atifi.library.author.service;

import com.atifi.library.author.dto.request.AuthorFilter;
import com.atifi.library.author.dto.request.CreateAuthorRequest;
import com.atifi.library.author.dto.request.UpdateAuthorRequest;
import com.atifi.library.author.dto.response.AuthorResponse;
import com.atifi.library.author.exception.AuthorNotFoundException;
import com.atifi.library.author.mapper.AuthorMapper;
import com.atifi.library.author.model.Author;
import com.atifi.library.author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.atifi.library.author.repository.AuthorSpecifications.hasName;
import static com.atifi.library.author.repository.AuthorSpecifications.hasStatus;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository repository;

    public List<AuthorResponse> findAll(AuthorFilter filters) {
        Specification<Author> spec = Specification.where(hasName(filters.name()))
                .and(hasStatus(filters.country()));

        List<Author> authors = repository.findAll(spec);
        return authors.stream().map(AuthorMapper::toResponse).toList();
    }

    public AuthorResponse findById(Integer id) {
        Author author = repository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));

        return AuthorMapper.toResponse(author);
    }

    public AuthorResponse save(CreateAuthorRequest request) {
        Author author = AuthorMapper.toEntity(request);
        Author savedAuthor = repository.save(author);

        return AuthorMapper.toResponse(savedAuthor);
    }

    public AuthorResponse update(Integer id, UpdateAuthorRequest request) {
        Author author = repository.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
        AuthorMapper.updateEntity(author, request);

        Author updatedAuthor = repository.save(author);
        return AuthorMapper.toResponse(updatedAuthor);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

}
