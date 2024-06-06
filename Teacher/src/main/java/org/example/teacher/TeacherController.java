package org.example.teacher;

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
@RequestMapping("/teacher")
public class TeacherController {
    private final TeacherServices teacherServices;
    private final ObjectMapper objectMapper;

    public TeacherController(TeacherServices teacherServices, ObjectMapper objectMapper) {
        this.teacherServices = teacherServices;
        this.objectMapper = objectMapper;
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
    @PatchMapping("/{id}")
    ResponseEntity<TeacherDto> changData(@PathVariable Long id, @RequestBody JsonMergePatch patch) {
        TeacherDto teacher;
        try {
            TeacherDto teacherById = teacherServices.findById(id);
            TeacherDto teacherDto = applyPatch(teacherById, patch);
             teacher =  teacherServices.changeData(teacherDto,id);


        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.internalServerError().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(teacher);
    }


    private TeacherDto applyPatch(TeacherDto dto, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode studentNode = objectMapper.valueToTree(dto);
        JsonNode jobOfferPatchedNode = patch.apply(studentNode);
        return objectMapper.treeToValue(jobOfferPatchedNode, TeacherDto.class);
    }
    @DeleteMapping("/{id}")
    ResponseEntity<?>deleteById(@PathVariable long id){
        teacherServices.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
