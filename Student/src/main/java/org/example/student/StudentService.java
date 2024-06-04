package org.example.student;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final WebClient.Builder webClientBuilder;
    @Value("${course}")
    private String courseUrl;
    @Value("${teacher}")
    private String teacherUrl;


    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper, WebClient.Builder webClientBuilder) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.webClientBuilder = webClientBuilder;
    }

    StudentDto addStudent(StudentDto dto) {
        Student student = studentMapper.dtoToEntity(dto, course(dto.course()));
        Student save = studentRepository.save(student);
        return studentMapper.entityToDto(save);

    }
    StudentDto changeData(StudentDto dto,long id){
        Student student = studentMapper.dtoToEntity(dto, course(dto.course()));
        student.setId(id);

        Student save = studentRepository.save(student);
        return studentMapper.entityToDto(save);
    }

    private String course(String course) {
        return webClientBuilder.baseUrl(courseUrl).build().get().uri("/course/name/{course}", course).retrieve().bodyToMono(Course.class).map(Course::course).flux().blockFirst();


    }

    private String findByFirstnameAndLastname(String firstName, String lastName) {
        return studentRepository.findByFirstNameAndLastName(firstName, lastName).map(Student::getCourse).orElseThrow();

    }

    Mono<List<Teacher>> findTeachersByStudent(String firstName, String lastName) {
        String curseName = findByFirstnameAndLastname(firstName, lastName);
        return webClientBuilder.baseUrl(courseUrl).build().get().uri("/course/name/{curseName}", curseName).retrieve().bodyToMono(Subject.class).map(Subject::subject).flatMapMany(Flux::fromIterable).flatMap(subject -> webClientBuilder.baseUrl(teacherUrl).build().get().uri("/teacher/subject/{subject}", subject).retrieve().bodyToMono(Teacher.class)).distinct().collectList();

    }


    FindStudentByCourse findStudentByCourse(String course) {
        List<Student> byCourse = studentRepository.findByCourse(course);
        return studentMapper.findStudentByCourse(byCourse);
    }
    void deleteById(long id){
        studentRepository.deleteById(id);
    }
    StudentDto findStudentByFirstAndLastName(String firstName, String lastName){
        Student student = studentRepository.findByFirstNameAndLastName(firstName, lastName).orElseThrow();
        return studentMapper.entityToDto(student);
    }
    List<StudentDto>findAll(){
        return studentRepository.findAll().stream().map(studentMapper::entityToDto).toList();
    }
    StudentDto findStudentById(long id){
        return studentRepository.findById(id).map(studentMapper::entityToDto).orElseThrow();
    }


}
