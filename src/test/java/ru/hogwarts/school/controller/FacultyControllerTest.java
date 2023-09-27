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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SchoolApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {
    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    TestRestTemplate template;

    @BeforeEach
    public void setup() {
        baseUrl = "http://localhost:" + port + "/faculty";
    }

    @Test
    void testCrudFaculty() {
        Faculty faculty = new Faculty();
        faculty.setColor("red");
        faculty.setName("math");

        //create
        ResponseEntity<Faculty> response = template.postForEntity("/faculty", faculty, Faculty.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Faculty respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.getId()).isNotNull();
        assertThat(respBody.getName()).isEqualTo("math");
        assertThat(respBody.getColor()).isEqualTo("red");

        //read
        response = template.getForEntity("/faculty/" + respBody.getId(), Faculty.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.getId()).isNotNull();
        assertThat(respBody.getName()).isEqualTo("math");
        assertThat(respBody.getColor()).isEqualTo("red");

        //update
        faculty = new Faculty();
        faculty.setColor("red");
        faculty.setName("math");


        template.put("/faculty/" + respBody.getId(), faculty);


        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        respBody = response.getBody();
        assertThat(respBody).isNotNull();
        assertThat(respBody.getId()).isNotNull();
        assertThat(respBody.getName()).isEqualTo("math");
        assertThat(respBody.getColor()).isEqualTo("red");

        //delete
        template.delete("/faculty/" + respBody.getId());
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

  //  @Test
  //  public void testDeleteFaculty() {
  //      Faculty faculty = new Faculty("math", "red");

  //      template.delete(baseUrl, faculty, Faculty.class);

  //      ResponseEntity<Faculty> response = template.exchange("/faculty/{id}", HttpMethod.DELETE, null, Faculty.class, 1L);
  //      assertEquals(HttpStatus.OK, response.getStatusCode());
  //      assertNotNull(response.getBody());
  //      assertEquals("math", response.getBody().getName());
  //      assertEquals("red", response.getBody().getColor());
  //  }

    @Test
    public void testGetByColor() {
        Faculty faculty = new Faculty("Engineering", "Blue");

        template.postForEntity(baseUrl, faculty, Faculty.class);

        ResponseEntity<List<Faculty>> response = template
                .exchange("/faculty/by-color?color={color}"
                        , HttpMethod.GET, null
                        , new ParameterizedTypeReference<List<Faculty>>() {}, "Blue");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Engineering", response.getBody().get(0).getName());
        assertEquals("Blue", response.getBody().get(0).getColor());

    }

    @Test
    public void testGetByColorOrName() {
        Faculty faculty1 = new Faculty("Engineering", "Blue");

        template.postForEntity(baseUrl, faculty1, Faculty.class);

        ResponseEntity<List<Faculty>> response = template
                .exchange("/faculty/by-color?color={color}"
                        , HttpMethod.GET, null
                        , new ParameterizedTypeReference<List<Faculty>>() {}, "Blue");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Engineering", response.getBody().get(0).getName());
        assertEquals("Blue", response.getBody().get(0).getColor());

    }

    @Test
    public void testGetStudentsById() {
        Faculty faculty = new Faculty("Engineering", "Blue");
        Student student1 = new Student("John", 20);
        Student student2 = new Student("Jane", 21);

        template.postForEntity(baseUrl, faculty, Faculty.class);

        String studentsUrl = baseUrl + "/students-by-id?id=" + faculty.getId();

        template.postForEntity(studentsUrl, student1, Student.class);
        template.postForEntity(studentsUrl, student2, Student.class);

        ResponseEntity<List<Student>> response = template.exchange(
                studentsUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Student>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Student> students = response.getBody();
    }

}