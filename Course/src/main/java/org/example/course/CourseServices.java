package org.example.course;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class CourseServices {

    private final CourseRepository repository;
    private final CourseMapper courseMapper;
    private final WebClient webClient;


    public CourseServices(CourseRepository repository, CourseMapper courseMapper, WebClient.Builder webClient) {
        this.repository = repository;
        this.courseMapper = courseMapper;
        this.webClient = webClient.build();

    }

    CourseDto newCourse(CourseDto dto) {
        UniversityCourse course = courseMapper.dtoToEntity(dto, listCourse(dto.subject()));
        UniversityCourse save = repository.save(course);
        return courseMapper.entityToDto(save);
    }
    CourseDto changeData(CourseDto dto,long id){
        UniversityCourse universityCourse = courseMapper.dtoToEntity(dto, listCourse(dto.subject()));
        universityCourse.setId(id);
        UniversityCourse save = repository.save(universityCourse);
        return courseMapper.entityToDto(save);

    }


    private List<String> listCourse(List<String> subject) {

        return subject.stream().map(c -> webClient.get().uri("/subject/{subject}", c).retrieve().bodyToMono(Subject.class).map(Subject::subject).block()).toList();
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
