package org.example.course;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
        UniversityCourse course = courseMapper.dtoToEntity(dto, listCourse(dto.subjectId()));
        UniversityCourse save = repository.save(course);
        return courseMapper.entityToDto(save);
    }


    private List<Long> listCourse(List<Long> course) {
        List<Long> longListCourse = new ArrayList<>();

        for (Long l : course) {
            Long forObject = restTemplate.getForObject(COURSE_URL + l, Long.class);
            longListCourse.add(forObject);
        }
        return longListCourse;
    }
}
