package com.atifi.library.author.dto.response;

import lombok.Builder;

@Builder
public record AuthorResponse(
        Integer id,
        String name,
        String country
) {
}
