package org.example.teacher;

import org.springframework.stereotype.Service;

@Service
public class TeacherMapper {
    Teacher dtoToEntity(TeacherDto dto){
        Teacher teacher = new Teacher();
        teacher.setFirstName(dto.firstName());
        teacher.setLastName(dto.lastName());
        teacher.setAge(dto.age());
        teacher.setEmail(dto.email());
        teacher.setSubject(dto.subject());
        return teacher;
    }
    TeacherDto entityToDto(Teacher teacher){
        return new TeacherDto(teacher.getFirstName(),teacher.getLastName(), teacher.getAge(), teacher.getEmail(),teacher.getSubject());
    }
}
