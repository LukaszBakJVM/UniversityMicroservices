package org.example.course;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseMapper {
    UniversityCourse dtoToEntity(CourseDto dto, List<String> list) {
        UniversityCourse course = new UniversityCourse();
        course.setCourse(dto.course());
        course.setSubjectName(list);
        return course;
    }

    CourseDto entityToDto(UniversityCourse course) {
        return new CourseDto(course.getCourse(), course.getSubjectName());
    }
}
