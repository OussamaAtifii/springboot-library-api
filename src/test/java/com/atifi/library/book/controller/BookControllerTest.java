package com.atifi.library.book.controller;

import com.atifi.library.auth.service.CustomUserDetailService;
import com.atifi.library.auth.service.JwtService;
import com.atifi.library.book.dto.request.CreateBookRequest;
import com.atifi.library.book.dto.request.UpdateBookRequest;
import com.atifi.library.book.dto.response.BookResponse;
import com.atifi.library.book.exception.BookNotFoundException;
import com.atifi.library.book.service.BookService;
import com.atifi.library.constants.ApiConstants;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @Test
    void shouldFindBookById() throws Exception {
        BookResponse response = BookResponse.builder()
                .id(1)
                .title("The Hobbit: or There and Back Again")
                .isbn("9780007458424")
                .publishedDate(LocalDate.of(2026, 7, 28))
                .price(BigDecimal.valueOf(102))
                .build();

        when(bookService.findById(1)).thenReturn(response);

        mockMvc.perform(get(ApiConstants.BOOKS_BASE + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("The Hobbit: or There and Back Again"))
                .andExpect(jsonPath("$.isbn").value("9780007458424"))
                .andExpect(jsonPath("$.price").value(102));

        verify(bookService).findById(1);
    }

    @Test
    void shouldReturnNotFoundWhenBookDoesNotExists() throws Exception {
        when(bookService.findById(999)).thenThrow(new BookNotFoundException(999));

        mockMvc.perform(get(ApiConstants.BOOKS_BASE + "/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.messages[0]").value("Book with id 999 not found"))
                .andExpect(jsonPath("$.path").value(ApiConstants.BOOKS_BASE + "/999"));

        verify(bookService).findById(999);
    }

    @Test
    void shouldCreateBook() throws Exception {
        BookResponse response = BookResponse.builder()
                .id(1)
                .title("The Hobbit: or There and Back Again")
                .isbn("9780007458424")
                .publishedDate(LocalDate.of(2026, 7, 28))
                .price(BigDecimal.valueOf(102))
                .build();

        when(bookService.save(any(CreateBookRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post(ApiConstants.BOOKS_BASE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "The Hobbit: or There and Back Again",
                                            "isbn": "9780007458424",
                                            "publishedDate": "2026-07-28",
                                            "price": 102,
                                            "authorId": 1
                                        }
                                        """
                                ))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("The Hobbit: or There and Back Again"))
                .andExpect(jsonPath("$.isbn").value("9780007458424"))
                .andExpect(jsonPath("$.price").value(102))
        ;

        ArgumentCaptor<CreateBookRequest> captor = ArgumentCaptor.forClass(CreateBookRequest.class);

        verify(bookService).save(captor.capture());

        CreateBookRequest request = captor.getValue();

        assertEquals("The Hobbit: or There and Back Again", request.title());
        assertEquals("9780007458424", request.isbn());
        assertEquals(BigDecimal.valueOf(102), request.price());
        assertEquals(LocalDate.of(2026, 7, 28), request.publishedDate());
        assertEquals(1, request.authorId());
    }

    @Test
    void shouldReturnBadRequestWhenTitleIsBlank() throws Exception {
        mockMvc.perform(
                        post(ApiConstants.BOOKS_BASE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "title": "",
                                            "isbn": "9780007458424",
                                            "publishedDate": "2026-07-28",
                                            "price": 102,
                                            "authorId": 1
                                        }
                                        """
                                ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", hasItem("title must not be blank")))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value(ApiConstants.BOOKS_BASE));

        verifyNoInteractions(bookService);
    }

    @Test
    void shouldUpdateBook() throws Exception {
        BookResponse response = BookResponse.builder()
                .id(1)
                .title("The Hobbit: or There and Back Again Updated")
                .isbn("9780007458424")
                .publishedDate(LocalDate.of(2026, 7, 28))
                .price(BigDecimal.valueOf(99))
                .build();

        when(bookService.update(eq(1), any(UpdateBookRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put(ApiConstants.BOOKS_BASE + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "The Hobbit: or There and Back Again Updated",
                                    "isbn": "9780007458424",
                                    "publishedDate": "2026-07-28",
                                    "price": 99,
                                    "authorId": 2
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("The Hobbit: or There and Back Again Updated"))
                .andExpect(jsonPath("$.isbn").value("9780007458424"))
                .andExpect(jsonPath("$.price").value(99));

        ArgumentCaptor<UpdateBookRequest> captor = ArgumentCaptor.forClass(UpdateBookRequest.class);

        verify(bookService).update(eq(1), captor.capture());

        UpdateBookRequest request = captor.getValue();

        assertEquals("The Hobbit: or There and Back Again Updated", request.title());
        assertEquals("9780007458424", request.isbn());
        assertEquals(BigDecimal.valueOf(99), request.price());
        assertEquals(LocalDate.of(2026, 7, 28), request.publishedDate());
    }

    @Test
    void shouldDeleteAuthor() throws Exception {
        mockMvc.perform(delete(ApiConstants.BOOKS_BASE + "/1"))
                .andExpect(status().isNoContent());

        verify(bookService).deleteById(1);
    }
}
