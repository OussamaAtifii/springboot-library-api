package com.atifi.library.author.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateAuthorRequest(
        @NotBlank
        @Size(min = 3, max = 60)
        String name,

        @NotBlank()
        String country
) {
}
