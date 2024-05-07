package org.example.teacher;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
    @GetMapping("/subject/{subject}")
    TeacherDto bySubject(@PathVariable String subject){
        return teacherServices.findTeacherBySubject(subject);
    }
    @GetMapping("/student")
    Mono<List<StudentsList>> findStudentByTeacher(@RequestParam String firstname, @RequestParam  String lastname){
        return teacherServices.findStudentByTeacher(firstname,lastname);
    }


}
