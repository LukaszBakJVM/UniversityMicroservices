package org.example.teacher;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherMapper {
    Teacher dtoToEntity(TeacherDto dto, List<String> list){
        Teacher teacher = new Teacher();
        teacher.setFirstName(dto.firstName());
        teacher.setLastName(dto.lastName());
        teacher.setAge(dto.age());
        teacher.setEmail(dto.email());
        teacher.setSubjectName(list);
        return teacher;
    }
    TeacherDto entityToDto(Teacher teacher){
        return new TeacherDto(teacher.getFirstName(),teacher.getLastName(), teacher.getAge(), teacher.getEmail(),teacher.getSubjectName());
    }
}
