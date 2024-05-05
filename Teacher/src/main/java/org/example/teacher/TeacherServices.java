package org.example.teacher;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    TeacherDto newTeacher(TeacherDto dto) {
        Teacher teacher = teacherMapper.dtoToEntity(dto, listSubject(dto.subject()));
        Teacher save = teacherRepository.save(teacher);
        return teacherMapper.entityToDto(save);
    }


    private List<String> listSubject(List<String> course) {
        return course.stream().map(c -> restTemplate.getForObject(SUBJECT_URL + c, Subject.class)).map(subject -> subject != null ? subject.subject() : null).toList();
    }

    List<TeacherDto> findAll() {
        return teacherRepository.findAll().stream().map(teacherMapper::entityToDto).toList();
    }

    TeacherDto findById(long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow();
        return teacherMapper.entityToDto(teacher);

    }

    TeacherDto findTeacherBySubject(String subject) {
        return teacherRepository.findTeachersBySubjectName(subject).map(teacherMapper::entityToDto).orElseThrow();
    }

    Set<StudentsList> findStudentByTeacher(String firstName, String lastName) {
        Set<StudentsList> students = new HashSet<>();
        Set<String> course = new HashSet<>();
        List<String> strings = teacherRepository.findTeachersByFirstNameAndLastName(firstName, lastName).map(Teacher::getSubjectName).orElseThrow();
        for (String s : strings) {
            String COURSE_URL = "http://Course//course/subject/";
            List<String> course1 = restTemplate.getForObject(COURSE_URL + s, Course.class).course();

            course.addAll(course1);
        }
        for (String s : course) {

            String STUDENT_URL = "http://Student//student/course/";
            StudentsList student = restTemplate.getForObject(STUDENT_URL + s, StudentsList.class);
            students.add(student);
        }
        return students;
    }
}
