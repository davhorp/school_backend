package com.school.app.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterPersonResponse(
        @JsonProperty("code_result")
        String codeResult,
        @JsonProperty("message_result")
        String messageResult
) {
}
