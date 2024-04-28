package org.example.student;

import org.springframework.stereotype.Service;

@Service
public class StudentMapper {
    Student dtoToEntity(StudentDto dto,String courseName) {
        Student student = new Student();
        student.setFirstName(dto.firstName());
        student.setLastName(dto.lastName());
        student.setAge(dto.age());
        student.setEmail(dto.email());
        student.setCourse(courseName);
        return student;
    }

    StudentDto entityToDto(Student student) {
        return new StudentDto(student.getFirstName(), student.getLastName(), student.getAge(), student.getEmail(), student.getCourse());
    }
}
