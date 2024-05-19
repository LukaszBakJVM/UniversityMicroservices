package org.example.course;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MySQLContainer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")

class CourseApplicationTests {
 //   private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.37-bookworm");
    @LocalServerPort
    private static int dynamicPort;
    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig()
                    .port(dynamicPort)
                    .usingFilesUnderDirectory("src/test/resources"))
            .build();
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    CourseRepository courseRepository;


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
    //    registry.add("spring.datasource.url", mysql::getJdbcUrl);
      //  registry.add("spring.datasource.username", mysql::getUsername);
      //  registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("baseUrl", wireMockServer::baseUrl);
    }






    @Test
    void createNewCourse() {


        String s = """
                {
                  "course": "Nazwa kursu",
                  "subject": ["Matematyka"]
                }""";
        webTestClient.post().uri("/course").contentType(MediaType.APPLICATION_JSON).bodyValue(s).exchange().expectStatus().isOk();
    }

 /*   @Test
    void recordWiremock() throws InterruptedException {
        System.out.println(wireMockServer.getPort());
        while (true) {
            Thread.sleep(4000);
        }

    }*/
}
