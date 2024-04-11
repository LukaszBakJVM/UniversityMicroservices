package org.example.subject;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface SubjectRepository extends PagingAndSortingRepository<UniversitySubject,Long> {
    UniversitySubject save(UniversitySubject universitySubject);
    UniversitySubject findById(long id);
}
