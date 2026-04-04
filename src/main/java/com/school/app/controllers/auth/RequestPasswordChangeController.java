package com.school.app.controllers.auth;

import com.school.app.dto.requets.PasswordChangeRequest;
import com.school.app.dto.response.RequestPasswordChangeResponse;
import com.school.app.services.config.RequestPasswordChangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class RequestPasswordChangeController {

    private final RequestPasswordChangeService requestPasswordChangeService;

    @PostMapping("/changed-password")
    public ResponseEntity<RequestPasswordChangeResponse> changedPassword(
            // @Valid
            @RequestBody PasswordChangeRequest data) {
        return ResponseEntity.ok(requestPasswordChangeService.requestPasswordChangeServiceMethod(data.email()));
    }
}
