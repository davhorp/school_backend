package com.school.app.controllers.user;

import com.school.app.repository.UsuarioRepository;
import com.school.app.services.user.RecommendUsernamesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class UsuarioController {

    private final RecommendUsernamesService recommendUsernamesService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/verificar-disponibilidad-username")
    public ResponseEntity<?> verificar(@RequestParam String username) {
//        boolean disponible = !usuarioRepository.existsByUsername(username);
//
//        if (disponible) {
//            return ResponseEntity.ok(Map.of("disponible", true));
//        }

        // Si no está disponible, enviamos las 3 sugerencias
        List<String> opciones = recommendUsernamesService.obtenerSugerencias(username);
        return ResponseEntity.ok(Map.of(
                "disponible", false,
                "sugerencias", opciones
        ));
    }
}
