package org.example.student;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
public class StudentService {
    private final String COURSE_URL = "http://Course//course/";
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final WebClient.Builder webClientBuilder;


    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper, WebClient.Builder webClientBuilder, ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.webClientBuilder = webClientBuilder.filter(lbFunction);
    }

    StudentDto addStudent(StudentDto dto) {
        Student student = studentMapper.dtoToEntity(dto, course(dto.course()));
        Student save = studentRepository.save(student);
        return studentMapper.entityToDto(save);


    }

    private String course(String course) {

        return webClientBuilder.baseUrl(COURSE_URL).build().get().uri("name/{course}", course).retrieve().bodyToMono(Course.class).map(Course::course).flux().blockFirst();


    }

    String findByFirstnameAndLastname(String firstName, String lastName) {
        return studentRepository.findByFirstNameAndLastName(firstName, lastName).map(Student::getCourse).orElseThrow();

    }

    Mono<List<Teacher>> findTeachersByStudent(String firstName, String lastName) {
        String TEACHER_URL = "http://Teacher//teacher/subject/";

        String curseName = findByFirstnameAndLastname(firstName, lastName);

        return webClientBuilder.baseUrl(COURSE_URL).build().get().uri("name/{curseName}", curseName).retrieve().bodyToMono(Subject.class).map(Subject::subject).flatMapMany(Flux::fromIterable).flatMap(subject -> webClientBuilder.baseUrl(TEACHER_URL).build().get().uri("{subject}", subject).retrieve().bodyToMono(Teacher.class)).distinct().collectList();

    }


    FindStudentByCourse findStudentByCourse(String course) {
        List<Student> byCourse = studentRepository.findByCourse(course);
        return studentMapper.findStudentByCourse(byCourse);


    }

}
