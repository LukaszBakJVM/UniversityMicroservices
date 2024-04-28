package org.example.course;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CourseServices {
    private final String COURSE_URL = "http://Subject/subject/";
    private final CourseRepository repository;
    private final CourseMapper courseMapper;
    private final RestTemplate restTemplate;

    public CourseServices(CourseRepository repository, CourseMapper courseMapper, RestTemplate restTemplate) {
        this.repository = repository;
        this.courseMapper = courseMapper;
        this.restTemplate = restTemplate;
    }

    CourseDto newCourse(CourseDto dto) {
        UniversityCourse course = courseMapper.dtoToEntity(dto, listCourse(dto.subject()));
        UniversityCourse save = repository.save(course);
        return courseMapper.entityToDto(save);
    }


    private List<String> listCourse(List<String> course) {
        return course.stream().map(c -> restTemplate.getForObject(COURSE_URL + c, Subject.class)).map(subject -> subject != null ? subject.subject() : null).toList();


    }
    List<CourseDto>finaAll(){
        Sort subject = Sort.by(Sort.Direction.ASC, "course");
        return repository.findAll(subject).stream().map(courseMapper::entityToDto).toList();
    }
    CourseDto findById(long id){
        UniversityCourse course = repository.findById(id).orElseThrow();
        return courseMapper.entityToDto(course);
    }
    CourseDto findByCourse(String course){
        UniversityCourse courseName = repository.findByCourse(course).orElseThrow();
        return courseMapper.entityToDto(courseName);
    }

}
