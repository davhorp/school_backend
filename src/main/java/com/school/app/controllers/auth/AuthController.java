package com.school.app.controllers.auth;

import com.school.app.dto.requets.AuthRequest;
import com.school.app.services.auth.AuthService;
import com.school.app.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
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
        final LoginResponse response = service.authenticateUser(request.email(), request);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/refresh-token")
//    public TokenResponse refreshToken(
//            @RequestHeader(HttpHeaders.AUTHORIZATION) final String authentication
//    ) {
//        return service.refreshToken(authentication);
//    }


}
