package com.atifi.library.author.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateAuthorRequest(
        @NotBlank
        String name,

        @NotBlank
        String country
) {
}
