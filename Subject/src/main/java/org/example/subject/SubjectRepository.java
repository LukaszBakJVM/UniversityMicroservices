package org.example.subject;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SubjectRepository extends PagingAndSortingRepository<UniversitySubject, Long> {
    UniversitySubject save(UniversitySubject universitySubject);

    UniversitySubject findBySubject(String subject);

    List<UniversitySubject> findAll(Sort sort);
}
