package org.example.student;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    private final ObjectMapper objectMapper;


    public StudentController(StudentService studentService, ObjectMapper objectMapper) {
        this.studentService = studentService;
        this.objectMapper = objectMapper;
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
    ResponseEntity<?> deleteById(@PathVariable long id) {
        studentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    StudentDto findStudentByFirstAndLastname(@RequestParam String firstname, @RequestParam String lastname) {
        return studentService.findStudentByFirstAndLastName(firstname, lastname);
    }

    @GetMapping
    List<StudentDto> findAllStudents() {
        return studentService.findAll();
    }

    @PatchMapping("/{id}")
    ResponseEntity<StudentDto> changData(@PathVariable Long id, @RequestBody JsonMergePatch patch) {
        StudentDto student;
        try {
            StudentDto studentById = studentService.findStudentById(id);
            StudentDto studentDto = applyPatch(studentById, patch);
            student = studentService.changeData(studentDto, id);

        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.internalServerError().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(student);
    }


    private StudentDto applyPatch(StudentDto dto, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode studentNode = objectMapper.valueToTree(dto);
        JsonNode jobOfferPatchedNode = patch.apply(studentNode);
        return objectMapper.treeToValue(jobOfferPatchedNode, StudentDto.class);
    }
}
