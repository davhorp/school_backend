package com.school.app.dto.requets;

public record AuthRequest(
        String email,
        String password
) {
}
