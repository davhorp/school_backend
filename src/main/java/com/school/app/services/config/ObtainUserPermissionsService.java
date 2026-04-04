package com.school.app.services.config;

import com.school.app.entity.Permiso;
import com.school.app.entity.RolPermiso;
import com.school.app.repository.RolPermisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ObtainUserPermissionsService {

    private final RolPermisoRepository rolPermisoRepository;

    public List<String> obtenerPermisosDelUsuario(Integer idUsuario) {
        // Obtenemos los nombres directamente
        return rolPermisoRepository.findNombresPermisosByUsuario(idUsuario);
    }

    public List<Permiso> obtenerDetallePermisos(Integer idUsuario) {
        // Obtenemos la entidad RolPermiso y extraemos el objeto Permiso
        return rolPermisoRepository.findAllPermisosByUsuario(idUsuario)
                .stream()
                .map(RolPermiso::getPermiso)
                .toList();
    }
}
