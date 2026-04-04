package com.school.app.services.config;

import com.school.app.entity.RolPermiso;
import com.school.app.entity.RolPermisoId;
import com.school.app.repository.RolPermisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AsignarPermisosUsuarioService {

    private final RolPermisoRepository rolPermisoRepository;

    public void asignarPermisoAUsuario(Integer idRol, Integer idPermiso, Integer idUsuario) {
        // 1. Crear el ID compuesto
        RolPermisoId nuevoId = new RolPermisoId();
        nuevoId.setIdRol(idRol);
        nuevoId.setIdPermiso(idPermiso);
        nuevoId.setIdUsuario(idUsuario);

        // 2. Crear la entidad y asignar el ID
        RolPermiso nuevoPermiso = new RolPermiso();
        nuevoPermiso.setId(nuevoId);

        // Nota: Si tus entidades tienen las relaciones ManyToOne mapeadas,
        // podrías necesitar setear los objetos completos si JPA lo requiere:
        // nuevoPermiso.setUsuario(usuarioRepo.findById(idUsuario).get());

        // 3. Guardar (Esto ejecutará el INSERT en Postgres)
        rolPermisoRepository.save(nuevoPermiso);
    }
}
