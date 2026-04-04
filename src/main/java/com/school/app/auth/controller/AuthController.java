package com.school.app.auth.controller;

import com.school.app.auth.service.AuthService;
import com.school.app.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody AuthRequest request) {
        final LoginResponse response = service.authenticateUser(request);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/refresh-token")
//    public TokenResponse refreshToken(
//            @RequestHeader(HttpHeaders.AUTHORIZATION) final String authentication
//    ) {
//        return service.refreshToken(authentication);
//    }


}
