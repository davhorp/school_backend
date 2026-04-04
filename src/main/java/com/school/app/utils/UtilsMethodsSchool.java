package com.school.app.utils;

import com.school.app.entity.Usuario;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class UtilsMethodsSchool {

    public String getNameFullUser(Usuario usr){
        return usr.getPersona().getApellidoPaterno()
                .concat(" ")
                .concat(usr.getPersona().getApellidoMaterno())
                .concat(" ")
                .concat(usr.getPersona().getNombre());
    }

    public String generarCodigoVerificacion() {
        SecureRandom random = new SecureRandom();
        int codigo = 10000000 + random.nextInt(90000000);
        return String.valueOf(codigo);
    }
}
