package org.example.student;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends PagingAndSortingRepository<Student,Long> {
    Optional<Student>findByFirstNameAndLastName(String firstName,String lastName);
    Student save(Student student);
    List<Student>findByCourse(String course);


}
