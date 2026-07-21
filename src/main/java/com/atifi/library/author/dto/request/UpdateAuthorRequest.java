package com.atifi.library.author.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateAuthorRequest(
        @NotBlank
        @Size(min = 3, max = 60)
        String name,

        @NotBlank
        String country
) {
}
