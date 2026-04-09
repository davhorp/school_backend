package com.school.app.mapper;

import com.school.app.dto.response.LoginResponse;
import com.school.app.dto.response.ProfileDetailsResponse;
import com.school.app.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoginMapper {

    @Mapping(target = "accessToken", source = "accessToken")
    @Mapping(target = "refreshToken", source = "refreshToken")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "sessionActive", source = "user.activo")
    @Mapping(target = "id", source = "user.idUsuario")
    // Mapeo del nombre completo usando el método que agregamos a la Entidad
    @Mapping(target = "nameFull", expression = "java(user.getNombreCompleto())")
    // Mapeo del objeto anidado ProfileDetailsResponse
    //@Mapping(target = "profile", source = "user", qualifiedByName = "toProfileDetails")
    @Mapping(target = "profile", expression = "java(toProfileDetails(user, permissions))")
    LoginResponse toLoginResponse(Usuario user, String accessToken, String refreshToken, List<String> permissions);

    // Método de apoyo para construir los detalles del perfil
    //@Named("toProfileDetails")// no se necesita si usamos expression
    default ProfileDetailsResponse toProfileDetails(Usuario user, List<String> permissions) {
        if (user == null || user.getRol() == null) return null;

        return new ProfileDetailsResponse(
                user.getRol().getNombreRol().name().toLowerCase(),
                permissions
        );
    }
}
