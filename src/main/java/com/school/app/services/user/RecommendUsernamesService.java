package com.school.app.services.user;

import com.school.app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RecommendUsernamesService {

    private final UsuarioRepository usuarioRepository;
    private final Random random = new Random();

    public List<String> obtenerSugerencias(String usernameOriginal) {
        List<String> sugerencias = new ArrayList<>();
        // Limpiamos el nombre: minúsculas, sin espacios y sin caracteres especiales simples
        String base = usernameOriginal.toLowerCase().trim().replaceAll("[^a-z0-9]", "");
        int intentos = 0;
        // Buscamos hasta tener 3 opciones válidas o alcanzar un límite de intentos
        while (sugerencias.size() < 20 && intentos < 20) {
            String propuesta = generarVariante(base, intentos);
            if (!usuarioRepository.existsByUsername(propuesta) && !sugerencias.contains(propuesta)) {
                sugerencias.add(propuesta);
            }
            intentos++;
        }
        return sugerencias;
    }

    private String generarVariante(String base, int vuelta) {
        int anioActual = Year.now().getValue();
        return switch (vuelta % 5) {
            case 0 -> base + random.nextInt(10, 99);          // ej: davhorp25
            case 1 -> base + "." + random.nextInt(1, 9);       // ej: davhorp.3
            case 2 -> base + "_" + anioActual;                 // ej: davhorp_2026
            case 3 -> base + "mx";                             // ej: davhorpmx
            case 4 -> "__" + base;                            // ej: __davhorp
            default -> base + random.nextInt(100, 999);
        };
    }
}
