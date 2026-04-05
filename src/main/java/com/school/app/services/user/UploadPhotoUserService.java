package com.school.app.services.user;

import com.school.app.entity.Usuario;
import com.school.app.exceptions.PhotoUserErrorException;
import com.school.app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class UploadPhotoUserService {

    @Value("${media.location}")
    private String UPLOAD_DIR;

    private final UsuarioRepository usuarioRepository;
    private final FindUserByEmailService findUserByEmailService;

    public void uploadPhotoUserMethod(String email, MultipartFile file) throws IOException {
        Usuario usr = findUserByEmailService.findUserByEmailMethod(email);
        if(file.isEmpty())
            throw new PhotoUserErrorException("El archivo no puede estar vacio, dañado, favor de revisar");
        String fileName = usr.getIdUsuario() + "_" + System.currentTimeMillis() + ".jpg";
        Path path = Paths.get(UPLOAD_DIR.concat(fileName));
        Files.createDirectories(path.getParent());
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        usr.setFotoPerfil(fileName);
        usuarioRepository.save(usr);
    }
}
