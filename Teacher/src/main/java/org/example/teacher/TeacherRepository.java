package org.example.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findById(long id);
    Optional<Teacher>findTeachersBySubjectName(String subject);
    Optional<Teacher>findTeachersByFirstNameAndLastName(String firstName, String lastName);
    @Query("SELECT COUNT(u) FROM Teacher u")
    long countAll();
}
