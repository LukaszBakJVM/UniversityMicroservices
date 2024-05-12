package org.example.subject;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServices {
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    public SubjectServices(SubjectRepository subjectRepository, SubjectMapper subjectMapper) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
    }

    SubjectDto saveSubject(SubjectDto dto) {
        UniversitySubject universitySubject = subjectMapper.dtoToEntity(dto);
        UniversitySubject save = subjectRepository.save(universitySubject);
        return subjectMapper.entityToDto(save);
    }

    SubjectDto findBySubject(String subject) {
        UniversitySubject bySubject = subjectRepository.findBySubject(subject);
        return subjectMapper.entityToDto(bySubject);
    }

    List<SubjectDto> findAllAndSortBySubject() {
        Sort subject = Sort.by(Sort.Direction.ASC, "subject");
        return subjectRepository.findAll(subject).stream().map(subjectMapper::entityToDto).toList();

    }
}
