package org.example.subject;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MySQLContainer;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class UniversitySubjectApplicationTests {
    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.37-bookworm").withInitScript("schema.sql");
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    SubjectRepository subjectRepository;


    @BeforeAll
    static void beforeAll() {
        mysql.start();
    }

    @AfterAll
    static void afterAll() {
        mysql.stop();
    }


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @BeforeEach
    void cleanDatabase() {
        subjectRepository.deleteAll();
    }

    @Test
    void addNewSubjectTest() {
        String response = "{\"subject\":\"Matematyka\"}";

        SubjectDto subjectdto = new SubjectDto("Matematyka");

        webTestClient.post().uri("/subject/add").contentType(MediaType.APPLICATION_JSON).bodyValue(subjectdto).exchange().expectStatus().isOk().expectBody().json(response); //

    }

    @Test
    void findSubjectByName() {
        String response = "{\"subject\":\"Informatyka\"}";
        setupSubjects();
        String subjectName = "Informatyka";
        webTestClient.get().uri("subject/{subject}", subjectName).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody().json(response);

    }

    @Test
    void findAllSubjects() {
        String response = "[{\"subject\":\"Informatyka\"},{\"subject\":\"Mechanika\"}]";

        setupSubjects();
        webTestClient.get().uri("subject").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody().json(response);

    }

    void setupSubjects() {
        UniversitySubject informatyka = new UniversitySubject();
        informatyka.setSubject("Informatyka");
        subjectRepository.save(informatyka);
        UniversitySubject mechanika = new UniversitySubject();
        mechanika.setSubject("Mechanika");
        subjectRepository.save(mechanika);
    }


}
