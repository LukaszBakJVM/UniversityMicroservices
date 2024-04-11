package org.example.subject;

import org.springframework.stereotype.Service;

@Service
public class SubjectServices {
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    public SubjectServices(SubjectRepository subjectRepository, SubjectMapper subjectMapper) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
    }
    SubjectDto saveSubject(SubjectDto dto){
        UniversitySubject universitySubject = subjectMapper.dtoToEntity(dto);
        UniversitySubject save = subjectRepository.save(universitySubject);
        return subjectMapper.entityToDto(save);
    }
    SubjectDto findById(long id){
        UniversitySubject byId = subjectRepository.findById(id);
        return subjectMapper.entityToDto(byId);
    }
}
