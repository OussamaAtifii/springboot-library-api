package com.atifi.library.author.controller;

import com.atifi.library.auth.service.CustomUserDetailService;
import com.atifi.library.auth.service.JwtService;
import com.atifi.library.author.dto.request.CreateAuthorRequest;
import com.atifi.library.author.dto.request.UpdateAuthorRequest;
import com.atifi.library.author.dto.response.AuthorResponse;
import com.atifi.library.author.exception.AuthorNotFoundException;
import com.atifi.library.author.service.AuthorService;
import com.atifi.library.constants.ApiConstants;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @Test
    void shouldFindAuthorById() throws Exception {
        AuthorResponse author = new AuthorResponse(
                1,
                "J. R. R. Tolkien",
                "England"
        );

        when(authorService.findById(1)).thenReturn(author);

        mockMvc.perform(get(ApiConstants.AUTHORS_BASE + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("J. R. R. Tolkien"));

        verify(authorService).findById(1);
    }

    @Test
    void shouldReturnNotFoundWhenAuthorDoesNotExist() throws Exception {
        when(authorService.findById(999))
                .thenThrow(new AuthorNotFoundException(999));

        mockMvc.perform(get(ApiConstants.AUTHORS_BASE + "/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.messages[0]").value("Author with id 999 not found"))
                .andExpect(jsonPath("$.path").value(ApiConstants.AUTHORS_BASE + "/999"));

        verify(authorService).findById(999);
    }

    @Test
    void shouldCreateAuthor() throws Exception {
        AuthorResponse response = AuthorResponse.builder()
                .id(1)
                .name("J. R. R. Tolkien")
                .country("England")
                .build();

        when(authorService.save(any(CreateAuthorRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post(ApiConstants.AUTHORS_BASE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "J. R. R. Tolkien",
                                            "country": "England"
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("J. R. R. Tolkien"));

        ArgumentCaptor<CreateAuthorRequest> captor = ArgumentCaptor.forClass(CreateAuthorRequest.class);

        verify(authorService).save(captor.capture());

        CreateAuthorRequest request = captor.getValue();

        assertEquals("J. R. R. Tolkien", request.name());
        assertEquals("England", request.country());
    }

    @Test
    void shouldReturnBadRequestWhenAuthorNameAndAuthorCountryAreBlank() throws Exception {
        mockMvc.perform(
                        post(ApiConstants.AUTHORS_BASE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "",
                                            "country": ""
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.messages", hasItem("name must not be blank")))
                .andExpect(jsonPath("$.messages", hasItem("country must not be blank")))
                .andExpect(jsonPath("$.path").value(ApiConstants.AUTHORS_BASE));

        verifyNoInteractions(authorService);
    }

    @Test
    void shouldUpdateAuthor() throws Exception {
        AuthorResponse response = AuthorResponse.builder()
                .id(1)
                .name("J. R. R. Tolkien")
                .country("England")
                .build();

        when(authorService.update(eq(1), any(UpdateAuthorRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put(ApiConstants.AUTHORS_BASE + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "J. R. R. Tolkien",
                                    "country": "England"
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("J. R. R. Tolkien"))
                .andExpect(jsonPath("$.country").value("England"));

        ArgumentCaptor<UpdateAuthorRequest> captor = ArgumentCaptor.forClass(UpdateAuthorRequest.class);

        verify(authorService).update(eq(1), captor.capture());

        UpdateAuthorRequest request = captor.getValue();

        assertEquals("J. R. R. Tolkien", request.name());
        assertEquals("England", request.country());
    }

    @Test
    void shouldDeleteAuthor() throws Exception {
        mockMvc.perform(delete(ApiConstants.AUTHORS_BASE + "/1"))
                .andExpect(status().isNoContent());

        verify(authorService).deleteById(eq(1));
    }
}
