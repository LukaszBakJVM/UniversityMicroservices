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
    List<CourseDto> findById(@PathVariable long id) {
        return services.finaAllById(id);
    }

    @GetMapping
    List<CourseDto> finaAll() {
        return services.finaAll();
    }
}
