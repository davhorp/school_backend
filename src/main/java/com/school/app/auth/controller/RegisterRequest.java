package com.school.app.auth.controller;

public record RegisterRequest(
        String name,
        String email,
        String password
) {
}
