package org.example.student;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

    StudentDto addStudent(StudentDto dto) {
        Student student = studentMapper.dtoToEntity(dto, course(dto.course()));
        Student save = studentRepository.save(student);
        return studentMapper.entityToDto(save);


    }

    private String course(String course) {
        return restTemplate.getForObject(COURSE_URL + "name/" + course, Course.class).course();
    }

    String findByFirstnameAndLastname(String firstName, String lastName) {
        return studentRepository.findByFirstNameAndLastName(firstName, lastName).map(Student::getCourse).orElseThrow();

    }

    Set<Teacher> findTeachersByStudent(String firstName, String lastName) {
        Set<Teacher> teachers = new HashSet<>();
        String curseName = findByFirstnameAndLastname(firstName, lastName);
        List<String> subject = restTemplate.getForObject(COURSE_URL + "name/" + curseName, Subject.class).subject();
        for (String s : subject) {
            String TEACHER_URL = "http://Teacher//teacher/subject/";
            Teacher teacher = restTemplate.getForObject(TEACHER_URL + s, Teacher.class);
            teachers.add(teacher);
        }


        return teachers;


    }

}
