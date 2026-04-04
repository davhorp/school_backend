package com.school.app.dto.requets;

public record ValidateAndChangePasswordRequest(
        String code,
        String email,
        String newPassword
) {
}
