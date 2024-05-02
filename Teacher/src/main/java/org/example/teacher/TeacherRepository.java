package org.example.teacher;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findById(long id);
    Optional<Teacher>findTeachersBySubjectName(String subject);
}
