package com.school.app.dto.response;

public record LoginResponse (
        String accessToken,
        String refreshToken,
        String nameFull,
        ProfileDetailsResponse profile,
        String username,
        boolean sessionActive,
        Long id
) {
}
