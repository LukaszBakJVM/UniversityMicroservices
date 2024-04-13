package org.example.course;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CourseRepository extends PagingAndSortingRepository<UniversityCourse,Long> {
    UniversityCourse save(UniversityCourse course);
    List<UniversityCourse>findAllById(long id);
}
