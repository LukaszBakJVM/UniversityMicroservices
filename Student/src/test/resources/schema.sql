CREATE TABLE student (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          first_name VARCHAR(255) NOT NULL,
                          last_name VARCHAR(255) NOT NULL,
                          age INT NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          course VARCHAR(255) NOT NULL
);
