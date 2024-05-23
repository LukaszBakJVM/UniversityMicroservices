package org.example.student;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MySQLContainer;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class StudentApplicationTests {
    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.37-bookworm").withInitScript("schema.sql");
    @LocalServerPort
    private static int dynamicPort;
    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance().options(wireMockConfig().port(dynamicPort)).build();
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    StudentRepository studentRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("teacher", wireMockServer::baseUrl);
        registry.add("course", wireMockServer::baseUrl);


    }

    @BeforeAll
    static void beforeAll() {
        mysql.start();
    }

    @AfterAll
    static void afterAll() {
        mysql.stop();
    }

    @BeforeEach
    void cleanDatabase() {
        studentRepository.deleteAll();
    }

    @Test
    void createNewStudentWithCourse() {
        String request = """
                {"firstName":"Łukasz","lastName":"Bąk","age":22,"email":"rrr@w.pl","course":"Fiz-Chem"}""";

        webTestClient.post().uri("/student").contentType(MediaType.APPLICATION_JSON).bodyValue(request).exchange().expectStatus().isOk();
        long countStudent = studentRepository.countAll();

        assertEquals(1, countStudent);
    }

    @Test
    void findTeacherByStudent() {
        Student student = new Student();
        student.setFirstName("Lukasz");
        student.setLastName("Bak");
        student.setAge(22);
        student.setEmail("www@wp.pl");
        student.setCourse("Fiz-Chem");
        studentRepository.save(student);
        String firstname = "Lukasz";
        String lastname = "Bak";
        webTestClient.get().uri(uriBuilder -> uriBuilder.path("/student/teacher").queryParam("firstname", firstname).queryParam("lastname", lastname).build()).exchange().expectStatus().isOk();
    }

}
