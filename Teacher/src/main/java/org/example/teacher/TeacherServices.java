package org.example.teacher;

import jakarta.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
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
    private final LocalValidatorFactoryBean validation;
    @Value("${subject}")
    private String subjectUrl;
    @Value("${course}")
    private String courseUrl;
    @Value("${student}")
    private String studentUrl;

    public TeacherServices(TeacherRepository teacherRepository, TeacherMapper teacherMapper, WebClient.Builder webClientBuilder, LocalValidatorFactoryBean validation) {

        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
        this.webClientBuilder = webClientBuilder;

        this.validation = validation;
    }

    TeacherDto newTeacher(TeacherDto dto) {
        Teacher teacher = teacherMapper.dtoToEntity(dto, listSubject(dto.subject()));
        validationTeacher(teacher);
        Teacher save = teacherRepository.save(teacher);
        return teacherMapper.entityToDto(save);
    }
    TeacherDto changeData(TeacherDto dto,long id){
        Teacher teacher = teacherMapper.dtoToEntity(dto, listSubject(dto.subject()));
        validationTeacher(teacher);
        teacher.setId(id);
        Teacher save = teacherRepository.save(teacher);
        return teacherMapper.entityToDto(save);
    }


    private List<String> listSubject(List<String> course) {
        return course.stream().map(c -> webClientBuilder.baseUrl(subjectUrl+"/subject/").build().get().uri(c).retrieve().bodyToMono(Subject.class).map(Subject::subject).block()).toList();

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
        Mono<Set<String>> coursesMono = teacherRepository.findTeachersByFirstNameAndLastName(firstName, lastName).map(Teacher::getSubjectName).map(subjectNames -> Flux.fromIterable(subjectNames).flatMap(subject -> webClientBuilder.build().get().uri(courseUrl+"/course/subject/" + subject).retrieve().bodyToMono(Course.class)).flatMap(c -> Flux.fromIterable(c.course())).collect(Collectors.toSet())).orElseThrow();
        Flux<String> coursesFlux = coursesMono.flatMapMany(Flux::fromIterable);
        Flux<StudentsList> studentsFlux = coursesFlux.flatMap(c -> webClientBuilder.build().get().uri(studentUrl+"/student/course/" + c).retrieve().bodyToMono(StudentsList.class));
        return studentsFlux.collectList();
    }
    void  deleteById(long id){
        teacherRepository.deleteById(id);
    }
    private void validationTeacher(Teacher teacher) {
        Set<ConstraintViolation<Teacher>> violations = validation.validate(teacher);
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Validation error occurred: ");
            for (ConstraintViolation<Teacher> violation : violations) {
                errorMessage.append(violation.getMessage()).append("; ");
            }

            throw new Validation(errorMessage.toString());

        }
    }
}
