CREATE TABLE university_course (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  course VARCHAR(255) NOT NULL
);

CREATE TABLE university_course_subject_name (
                                              university_course_id BIGINT NOT NULL,
                                              subject_name VARCHAR(255) NOT NULL,
                                              FOREIGN KEY (university_course_id) REFERENCES university_course(id)
);
