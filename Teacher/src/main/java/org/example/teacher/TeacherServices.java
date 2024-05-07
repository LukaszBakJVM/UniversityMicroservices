package org.example.teacher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeacherServices {
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;
    private final WebClient.Builder webClientBuilder;
    @Value("${subject}")
    private String subjectUrl;
    @Value("${course}")
    private String courseUrl;
    @Value("${student}")
    private String studentUrl;

    public TeacherServices(TeacherRepository teacherRepository, TeacherMapper teacherMapper, WebClient.Builder webClientBuilder, ReactorLoadBalancerExchangeFilterFunction lbFunction) {

        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
        this.webClientBuilder = webClientBuilder.filter(lbFunction);
    }

    TeacherDto newTeacher(TeacherDto dto) {
        Teacher teacher = teacherMapper.dtoToEntity(dto, listSubject(dto.subject()));
        Teacher save = teacherRepository.save(teacher);
        return teacherMapper.entityToDto(save);
    }


    private List<String> listSubject(List<String> course) {
        return course.stream().map(c -> webClientBuilder.baseUrl(subjectUrl).build().get().uri(c).retrieve().bodyToMono(Subject.class).map(Subject::subject).block()).toList();

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

    Mono<List<StudentsList>> findStudentByTeacher(String firstName, String lastName) {
        Mono<Set<String>> coursesMono = teacherRepository.findTeachersByFirstNameAndLastName(firstName, lastName).map(Teacher::getSubjectName).map(subjectNames -> Flux.fromIterable(subjectNames).flatMap(subject -> webClientBuilder.build().get().uri(courseUrl + subject).retrieve().bodyToMono(Course.class)).flatMap(c -> Flux.fromIterable(c.course())).collect(Collectors.toSet())).orElseThrow();
        Flux<String> coursesFlux = coursesMono.flatMapMany(Flux::fromIterable);
        Flux<StudentsList> studentsFlux = coursesFlux.flatMap(c -> webClientBuilder.build().get().uri(studentUrl + c).retrieve().bodyToMono(StudentsList.class));
        return studentsFlux.collectList();
    }
}
