package org.example.student;

import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    StudentDto save(@RequestBody StudentDto dto) {
        return studentService.addStudent(dto);
    }

    @GetMapping
    String findStudent(@RequestParam String firstname, @RequestParam String lastname) {
        return studentService.findByFirstnameAndLastname(firstname, lastname);
    }

    @GetMapping("/teacher")
    Set<Teacher> findTeachersByStudent(@RequestParam String firstname, @RequestParam String lastname) {
        return studentService.findTeachersByStudent(firstname, lastname);
    }

    @GetMapping("/course/{course}")
    FindStudentByCourse findStudentsByCourse(@PathVariable String course) {
        return studentService.findStudentByCourse(course);
    }
}
