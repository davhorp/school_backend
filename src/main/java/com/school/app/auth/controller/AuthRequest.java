package com.school.app.auth.controller;

public record AuthRequest(
        String email,
        String password
) {
}
