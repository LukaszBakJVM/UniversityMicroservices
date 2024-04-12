package org.example.course;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final CourseServices services;

    public CourseController(CourseServices services) {
        this.services = services;
    }
    @PostMapping
    CourseDto courseDto(@RequestBody CourseDto dto){
        return services.newCourse(dto);
    }
}
