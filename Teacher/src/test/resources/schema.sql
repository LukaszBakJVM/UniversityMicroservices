
CREATE TABLE teacher (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         first_name VARCHAR(255) NOT NULL,
                         last_name VARCHAR(255) NOT NULL,
                         age INT NOT NULL,
                         email VARCHAR(255) NOT NULL
);


CREATE TABLE teacher_subject_name (
                                      teacher_id BIGINT NOT NULL,
                                      subject_name VARCHAR(255) NOT NULL,
                                      FOREIGN KEY (teacher_id) REFERENCES teacher(id)
);
