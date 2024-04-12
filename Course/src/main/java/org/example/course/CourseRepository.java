package org.example.course;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CourseRepository extends PagingAndSortingRepository<UniversityCourse,Long> {
    UniversityCourse save(UniversityCourse course);
}
