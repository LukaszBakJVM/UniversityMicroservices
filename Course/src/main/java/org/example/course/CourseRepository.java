package org.example.course;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends PagingAndSortingRepository<UniversityCourse, Long> {
    UniversityCourse save(UniversityCourse course);

   Optional<UniversityCourse> findById(long id);
   Optional<UniversityCourse>findByCourse(String course);

    List<UniversityCourse> findAll(Sort sort);
}
