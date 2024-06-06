package org.example.course;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final CourseServices services;
    private final ObjectMapper objectMapper;

    public CourseController(CourseServices services, ObjectMapper objectMapper) {
        this.services = services;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    CourseDto courseDto(@RequestBody CourseDto dto) {
        return services.newCourse(dto);
    }

    @GetMapping("/{id}")
    CourseDto findById(@PathVariable("id") long id) {
        return services.findById(id);
    }

    @GetMapping("/name/{name}")
    CourseDto findByName(@PathVariable("name") String name) {
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
    @PatchMapping("/{id}")
    ResponseEntity<CourseDto> changData(@PathVariable Long id, @RequestBody JsonMergePatch patch) {
        CourseDto dto;
        try {
            CourseDto courseById = services.findById(id);
            CourseDto courseDto = applyPatch(courseById, patch);
            dto = services.changeData(courseDto, id);

        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.internalServerError().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(dto);
    }


    private CourseDto applyPatch(CourseDto dto, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode studentNode = objectMapper.valueToTree(dto);
        JsonNode jobOfferPatchedNode = patch.apply(studentNode);
        return objectMapper.treeToValue(jobOfferPatchedNode, CourseDto.class);
    }
    @DeleteMapping("/{id}")
    ResponseEntity<?>deleteById(@PathVariable long id){
        services.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
