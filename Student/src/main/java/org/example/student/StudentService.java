package org.example.student;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class StudentService {
    private final String COURSE_URL = "http://Course//course/";
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final RestTemplate restTemplate;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper, RestTemplate restTemplate) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.restTemplate = restTemplate;
    }
    StudentDto addStudent(StudentDto dto){
        Student student = studentMapper.dtoToEntity(dto, course(dto.course()));
        Student save = studentRepository.save(student);
        return studentMapper.entityToDto(save);


    }
    private String course(String course) {
     return    restTemplate.getForObject(COURSE_URL +"name/"+ course, Course.class).course();
    }
    String findByFirstnameAndLastname(String firstName,String lastName){
     return    studentRepository.findByFirstNameAndLastName(firstName, lastName).map(Student::getCourse).orElseThrow();

    }
}
