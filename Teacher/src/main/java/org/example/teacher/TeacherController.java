package org.example.teacher;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    private final TeacherServices teacherServices;

    public TeacherController(TeacherServices teacherServices) {
        this.teacherServices = teacherServices;
    }

    @PostMapping()
    TeacherDto addTeacher(@RequestBody TeacherDto dto) {
        return teacherServices.newTeacher(dto);
    }

    @GetMapping("/{id}")
    TeacherDto findById(@PathVariable long id) {
        return teacherServices.findById(id);
    }

    @GetMapping("all")
    List<TeacherDto> all() {
        return teacherServices.findAll();
    }

}
