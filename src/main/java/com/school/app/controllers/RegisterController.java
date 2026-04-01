package com.school.app.controllers;

import com.school.app.dto.requets.PersonRequest;
import com.school.app.dto.response.RegisterPersonResponse;
import com.school.app.services.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/register")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping("/user")
    public ResponseEntity<RegisterPersonResponse> register(
           // @Valid
            @RequestBody PersonRequest data) {
        System.out.println(data.toString());
        return ResponseEntity.ok(registerService.registerUser(data));
    }
}
