package org.example.teacher;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TeacherServices {
    private final String SUBJECT_URL = "http://Subject/subject/";
    private final RestTemplate restTemplate;
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    public TeacherServices(RestTemplate restTemplate, TeacherRepository teacherRepository, TeacherMapper teacherMapper) {
        this.restTemplate = restTemplate;
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
    }

    TeacherDto newTeacher(TeacherDto dto){
        Teacher teacher = teacherMapper.dtoToEntity(dto, listSubject(dto.subject()));
        Teacher save = teacherRepository.save(teacher);
        return teacherMapper.entityToDto(save);
    }




    private List<String> listSubject(List<String> course) {
        return course.stream().map(c -> restTemplate.getForObject(SUBJECT_URL + c, Subject.class)).map(subject -> subject != null ? subject.subject() : null).toList();
    }
}
