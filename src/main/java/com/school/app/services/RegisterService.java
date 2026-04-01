package com.school.app.services;

import com.school.app.dto.PersonaDTO;
import com.school.app.dto.requets.PersonRequest;
import com.school.app.dto.response.RegisterPersonResponse;
import com.school.app.entity.Persona;
import com.school.app.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final PersonaRepository personaRepository;

    public RegisterPersonResponse registerUser(PersonRequest request){
        Persona person = personaRepository.save(new PersonaDTO().toEntity(request));
        System.out.println(person.toString());
        return new RegisterPersonResponse("00", "Success");
    }

}
