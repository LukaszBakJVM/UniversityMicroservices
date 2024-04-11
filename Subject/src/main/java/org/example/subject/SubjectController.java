package org.example.subject;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectServices services;

    public SubjectController(SubjectServices services) {
        this.services = services;
    }
    @PostMapping("/add")
    SubjectDto newSubject(@RequestBody SubjectDto dto){
        return services.saveSubject(dto);
    }
    @GetMapping("/{id}")
    SubjectDto findById(@PathVariable long id){
        return services.findById(id);
    }
}
