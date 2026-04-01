package com.school.app.dto;

import com.school.app.dto.requets.DireccionRequest;
import com.school.app.entity.Direccion;
import com.school.app.entity.Estado;
import com.school.app.entity.Persona;
import com.school.app.enums.DireccionType;

public class DireccionDTO {

    public Direccion toEntity(DireccionRequest dto, Persona person, Estado edo){
        Direccion direccion = new Direccion();
        direccion.setCalle(dto.calle());
        direccion.setColonia(dto.colonia());
        direccion.setPersona(person);
        if (dto.tipoDireccion() != null) {
            direccion.setTipo(DireccionType.valueOf(dto.tipoDireccion()));
        }
        direccion.setNumExt(dto.numExt());
        direccion.setNumInt(dto.numInt());
        direccion.setCodigoPostal(dto.cp());
        direccion.setReferencia(dto.referencia());
        direccion.setEstado(edo);
        return direccion;
    }
}
