package com.atifi.library.author.mapper;

import com.atifi.library.author.dto.request.CreateAuthorRequest;
import com.atifi.library.author.dto.request.UpdateAuthorRequest;
import com.atifi.library.author.dto.response.AuthorResponse;
import com.atifi.library.author.model.Author;

public class AuthorMapper {

    public static AuthorResponse toResponse(Author author) {
        return AuthorResponse.builder()
                .id(author.getId())
                .name(author.getName())
                .country(author.getCountry())
                .build();
    }

    public static Author toEntity(CreateAuthorRequest request) {
        return Author.builder()
                .name(request.name())
                .country(request.country())
                .build();
    }

    public static void updateEntity(Author author, UpdateAuthorRequest request) {
        author.setName(request.name());
        author.setCountry(request.country());
    }

}
