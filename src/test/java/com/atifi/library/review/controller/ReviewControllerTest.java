package com.atifi.library.review.controller;

import com.atifi.library.auth.service.CustomUserDetailService;
import com.atifi.library.auth.service.JwtService;
import com.atifi.library.constants.ApiConstants;
import com.atifi.library.review.dto.request.CreateReviewRequest;
import com.atifi.library.review.dto.request.UpdateReviewRequest;
import com.atifi.library.review.dto.response.ReviewResponse;
import com.atifi.library.review.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewService reviewService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @Test
    void shouldFindReviewsByBookId() throws Exception {
        List<ReviewResponse> response = List.of(
                ReviewResponse.builder()
                        .id(1L)
                        .rating(3)
                        .comment("Interesting book!")
                        .build(),
                ReviewResponse.builder()
                        .id(2L)
                        .rating(4)
                        .comment(null)
                        .build()
        );

        when(reviewService.findByBookId(1)).thenReturn(response);

        mockMvc.perform(get(ApiConstants.REVIEWS_BASE + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].rating").value(3))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].rating").value(4));

        verify(reviewService).findByBookId(1);
    }

    @Test
    void shouldCreateReview() throws Exception {
        ReviewResponse response = ReviewResponse.builder()
                .id(1L)
                .rating(3)
                .comment("Interesting book!")
                .build();

        when(reviewService.save(any(CreateReviewRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post(ApiConstants.REVIEWS_BASE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "bookId": 1,
                                            "rating": 3,
                                            "comment": "Interesting book!"
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rating").value(3))
                .andExpect(jsonPath("$.comment").value("Interesting book!"));

        ArgumentCaptor<CreateReviewRequest> captor = ArgumentCaptor.forClass(CreateReviewRequest.class);

        verify(reviewService).save(captor.capture());

        CreateReviewRequest request = captor.getValue();

        assertEquals(1, request.bookId());
        assertEquals(3, request.rating());
        assertEquals("Interesting book!", request.comment());
    }

    @Test
    void shouldReturnBadRequestWhenRatingIsNull() throws Exception {
        mockMvc.perform(
                        post(ApiConstants.REVIEWS_BASE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "bookId": 1,
                                            "comment": "Interesting book!"
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages", hasItem("rating must not be null")))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.path").value(ApiConstants.REVIEWS_BASE));

        verifyNoInteractions(reviewService);
    }

    @Test
    void shouldUpdateReview() throws Exception {
        ReviewResponse response = ReviewResponse.builder()
                .id(1L)
                .rating(4)
                .comment("Interesting book updated!")
                .build();

        when(reviewService.update(eq(1L), any(UpdateReviewRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        put(ApiConstants.REVIEWS_BASE + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "rating": 4,
                                            "comment": "Interesting book updated!"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rating").value(4))
                .andExpect(jsonPath("$.comment").value("Interesting book updated!"));

        ArgumentCaptor<UpdateReviewRequest> captor = ArgumentCaptor.forClass(UpdateReviewRequest.class);

        verify(reviewService).update(eq(1L), captor.capture());

        UpdateReviewRequest request = captor.getValue();

        assertEquals(4, request.rating());
        assertEquals("Interesting book updated!", request.comment());
    }
}
