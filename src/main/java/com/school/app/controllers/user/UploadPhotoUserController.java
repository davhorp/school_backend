package com.school.app.controllers.user;

import com.school.app.dto.response.CommonsResponse;
import com.school.app.exceptions.PhotoUserErrorException;
import com.school.app.services.user.UploadPhotoUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class UploadPhotoUserController {

    private final UploadPhotoUserService uploadPhotoUserService;

    @PostMapping("/upload-photo-profile")
    public ResponseEntity<CommonsResponse> changedPassword(
            @RequestParam("email") String email, @RequestParam("file") MultipartFile photo) throws IOException {
        try{
            uploadPhotoUserService.uploadPhotoUserMethod(email, photo);
            return ResponseEntity.ok(new CommonsResponse("00", "Foto actualizada con éxito"));
        }catch (Exception ex){
            throw new PhotoUserErrorException("Error al procesar la foto de perfil: " + ex.getMessage());
        }
    }
}
