package org.example.subject;

import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/{subject}")
    SubjectDto findBySubject(@PathVariable String subject){
        return services.findBySubject(subject);
    }
    @GetMapping()
    List<SubjectDto>all(){
        return services.findAllAndSortBySubject();
    }
}
