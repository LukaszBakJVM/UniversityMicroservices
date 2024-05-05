package org.example.course;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final CourseServices services;

    public CourseController(CourseServices services) {
        this.services = services;
    }

    @PostMapping
    CourseDto courseDto(@RequestBody CourseDto dto) {
        return services.newCourse(dto);
    }

    @GetMapping("/{id}")
    CourseDto findById(@PathVariable long id) {
        return services.findById(id);
    }

    @GetMapping("/name/{name}")
    CourseDto findByName(@PathVariable String name) {
        return services.findByCourse(name);
    }


    @GetMapping
    List<CourseDto> finaAll() {
        return services.finaAll();
    }

    @GetMapping("/subject/{name}")
    FindStudentByTeacher findCourseBySubjectName(@PathVariable String name) {
        return services.findBySubject(name);
    }
}
