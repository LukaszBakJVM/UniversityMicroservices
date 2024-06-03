package org.example.student;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

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



    @GetMapping("/teacher")
    Mono<List<Teacher>> findTeachersByStudent(@RequestParam String firstname, @RequestParam String lastname) {
        return studentService.findTeachersByStudent(firstname, lastname);
    }

    @GetMapping("/course/{course}")
    FindStudentByCourse findStudentsByCourse(@PathVariable String course) {
        return studentService.findStudentByCourse(course);
    }


    @DeleteMapping("/{id}")
    ResponseEntity<?>deleteById(@PathVariable long id){
        studentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    StudentDto findStudentByFirstAndLastname(@RequestParam String firstname, @RequestParam String lastname){
        return  studentService.findStudentByFirstAndLastName(firstname,lastname);
    }
}
