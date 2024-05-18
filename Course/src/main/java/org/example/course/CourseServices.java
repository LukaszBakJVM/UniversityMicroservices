package org.example.course;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class CourseServices {

    private final CourseRepository repository;
    private final CourseMapper courseMapper;
    private final WebClient webClient;
    @Value("${baseUrl}")
    private String baseUrl;


    public CourseServices(CourseRepository repository, CourseMapper courseMapper, ReactorLoadBalancerExchangeFilterFunction lbFunction, WebClient.Builder webClient) {
        this.repository = repository;
        this.courseMapper = courseMapper;
        this.webClient = webClient.filter(lbFunction).build();

    }

    CourseDto newCourse(CourseDto dto) {
        UniversityCourse course = courseMapper.dtoToEntity(dto, listCourse(dto.subject()));
        UniversityCourse save = repository.save(course);
        return courseMapper.entityToDto(save);
    }


    private List<String> listCourse(List<String> subject) {
        System.out.println(baseUrl+subject);
        return subject.stream()
                .map(c -> webClient.get().uri(baseUrl+"/subject/{subject}",c).retrieve().bodyToMono(Subject.class).map(Subject::subject).block())
                .toList();
    }

    List<CourseDto> finaAll() {
        Sort subject = Sort.by(Sort.Direction.ASC, "course");
        return repository.findAll(subject).stream().map(courseMapper::entityToDto).toList();
    }

    CourseDto findById(long id) {
        UniversityCourse course = repository.findById(id).orElseThrow();
        return courseMapper.entityToDto(course);
    }

    CourseDto findByCourse(String course) {
        UniversityCourse courseName = repository.findByCourse(course).orElseThrow();
        return courseMapper.entityToDto(courseName);
    }

    FindStudentByTeacher findBySubject(String subject) {
        List<UniversityCourse> bySubjectName = repository.findBySubjectName(subject);
        return courseMapper.findStudentByTeacherDto(bySubjectName);

    }
}
