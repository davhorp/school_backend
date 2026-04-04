package com.school.app.controllers.auth;

import com.school.app.dto.requets.PasswordChangeRequest;
import com.school.app.dto.requets.ValidateAndChangePasswordRequest;
import com.school.app.dto.response.RequestPasswordChangeResponse;
import com.school.app.dto.response.ValidateAndChangePasswordResponse;
import com.school.app.services.auth.ValidateAndChangePasswordRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class ResetPasswordController {

    private final ValidateAndChangePasswordRequestService validateAndChangePasswordRequestService;

    @PostMapping("/reset-password")
    public ResponseEntity<ValidateAndChangePasswordResponse> changedPassword(
            // @Valid
            @RequestBody ValidateAndChangePasswordRequest data) {
        return ResponseEntity.ok(validateAndChangePasswordRequestService.validateAndChangePasswordMethod(data));
    }
}
