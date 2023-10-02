package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.SchoolApplication;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = SchoolApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    TestRestTemplate template;

    @BeforeEach
    public void setup() {
        baseUrl = "http://localhost:" + port + "/student";
    }

    @Test
    void testCrudStudent() {
        Student student = new Student();
        student.setName("John");
        student.setAge(20);

        //create
        ResponseEntity<Student> response = template.postForEntity("/student", student, Student.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Student respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.getId()).isNotNull();
        assertThat(respBody.getName()).isEqualTo("John");
        assertThat(respBody.getAge()).isEqualTo(20);

        //read
        response = template.getForEntity("/student/" + respBody.getId(), Student.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.getId()).isNotNull();
        assertThat(respBody.getName()).isEqualTo("John");
        assertThat(respBody.getAge()).isEqualTo(20);

        //update
        student = new Student();
        student.setAge(20);
        student.setName("John");

        template.put("/student/" + respBody.getId(), student);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.getId()).isNotNull();
        assertThat(respBody.getName()).isEqualTo("John");
        assertThat(respBody.getAge()).isEqualTo(20);

    }

    @Test
    public void testDeleteStudent() {
        Student student = new Student("John", 20);

        template.postForEntity(baseUrl, student, Student.class);

        ResponseEntity<Student> response = template
                .exchange("/student/{id}"
                , HttpMethod.DELETE, null, Student.class, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getName());
        assertEquals(20, response.getBody().getAge());
    }

    @Test
    public void testGetByAge() {
        Student student = new Student("John", 20);

        template.postForEntity(baseUrl, student, Student.class);

        ResponseEntity<List<Student>> response = template
                .exchange("/student/by-age?age={age}"
                        , HttpMethod.GET, null
                        , new ParameterizedTypeReference<List<Student>>() {
                        }, 20);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("John", response.getBody().get(0).getName());
        assertEquals(20, response.getBody().get(0).getAge());
    }


    @Test
    public void testGetByAgeBetween() {
        ResponseEntity<List<Student>> response = template
                .exchange("/student/by-age-between?min={min}&max={max}"
                        , HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {}
                        , 18, 25);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

    }


}
