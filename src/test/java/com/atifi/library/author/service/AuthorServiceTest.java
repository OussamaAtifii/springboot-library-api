package com.atifi.library.author.service;

import com.atifi.library.author.dto.request.AuthorFilter;
import com.atifi.library.author.dto.request.CreateAuthorRequest;
import com.atifi.library.author.dto.request.UpdateAuthorRequest;
import com.atifi.library.author.dto.response.AuthorResponse;
import com.atifi.library.author.exception.AuthorNotFoundException;
import com.atifi.library.author.model.Author;
import com.atifi.library.author.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    AuthorRepository repository;

    @InjectMocks
    AuthorService service;

    @Test
    void shouldFindAllAuthors() {
        AuthorFilter filter = new AuthorFilter("Tolkien", "England");
        Pageable pageable = PageRequest.of(0, 10);

        Author author = Author.builder()
                .id(1)
                .name("J. R. R. Tolkien")
                .country("England")
                .build();

        Page<Author> page = new PageImpl<>(List.of(author));

        when(repository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(page);

        Page<AuthorResponse> response = service.findAll(filter, pageable);

        assertEquals(1, response.getTotalElements());

        AuthorResponse authorResponse = response.getContent().getFirst();

        assertEquals(1, authorResponse.id());
        assertEquals("J. R. R. Tolkien", authorResponse.name());
        assertEquals("England", authorResponse.country());

        verify(repository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void shouldFindAuthorById() {
        Author author = Author.builder()
                .id(1)
                .name("Tolkien")
                .country("England")
                .build();

        when(repository.findById(1))
                .thenReturn(Optional.of(author));

        AuthorResponse response = service.findById(1);

        assertEquals(1, response.id());
        assertEquals("Tolkien", response.name());
        assertEquals("England", response.country());

        verify(repository).findById(1);
    }

    @Test
    void shouldThrowAuthorNotFoundExceptionWhenAuthorDoesNotExist() {
        when(repository.findById(999))
                .thenReturn(Optional.empty());

        AuthorNotFoundException exception = assertThrows(
                AuthorNotFoundException.class,
                () -> service.findById(999)
        );

        assertEquals("Author with id 999 not found", exception.getMessage());

        verify(repository).findById(999);
    }

    @Test
    void shouldSaveAuthor() {
        CreateAuthorRequest request = CreateAuthorRequest.builder()
                .name("Tolkien")
                .country("England")
                .build();

        Author savedAuthor = Author.builder()
                .id(1)
                .name("Tolkien")
                .country("England")
                .build();

        when(repository.save(any(Author.class)))
                .thenReturn(savedAuthor);

        AuthorResponse response = service.save(request);

        ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);

        verify(repository).save(captor.capture());

        Author author = captor.getValue();

        assertEquals("Tolkien", author.getName());
        assertEquals("England", author.getCountry());

        assertEquals(1, response.id());
        assertEquals("Tolkien", response.name());
        assertEquals("England", response.country());
    }

    @Test
    void shouldUpdateAuthor() {
        UpdateAuthorRequest request = UpdateAuthorRequest.builder()
                .name("Tolkien updated")
                .country("England")
                .build();

        Author existingAuthor = Author.builder()
                .id(1)
                .name("Tolkien")
                .country("")
                .build();

        Author updatedAuthor = Author.builder()
                .id(1)
                .name("Tolkien updated")
                .country("England")
                .build();

        when(repository.findById(1)).thenReturn(Optional.of(existingAuthor));
        when(repository.save(any(Author.class))).thenReturn(updatedAuthor);

        AuthorResponse response = service.update(1, request);

        assertEquals("Tolkien updated", response.name());
        assertEquals("England", response.country());

        ArgumentCaptor<Author> captor = ArgumentCaptor.forClass(Author.class);
        verify(repository).save(captor.capture());

        Author author = captor.getValue();

        assertEquals("Tolkien updated", author.getName());
        assertEquals("England", author.getCountry());
    }

    @Test
    void shouldThrowAuthorNotFoundExceptionWhenUpdatingNonExistingAuthor() {
        UpdateAuthorRequest request = UpdateAuthorRequest.builder()
                .name("Updated")
                .country("England")
                .build();

        when(repository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(
                AuthorNotFoundException.class,
                () -> service.update(999, request)
        );

        verify(repository, never()).save(any());
    }

    @Test
    void shouldDeleteAuthorById() {
        service.deleteById(1);

        verify(repository).deleteById(1);
    }
}
