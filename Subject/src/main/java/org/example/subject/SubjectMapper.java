package org.example.subject;

import org.springframework.stereotype.Service;

@Service
public class SubjectMapper {
    UniversitySubject dtoToEntity(SubjectDto dto){
        UniversitySubject universitySubject = new UniversitySubject();
        universitySubject.setSubject(dto.subject());
        return universitySubject;
    }
    SubjectDto entityToDto(UniversitySubject universitySubject){
        return new SubjectDto(universitySubject.getSubject());
    }
}
