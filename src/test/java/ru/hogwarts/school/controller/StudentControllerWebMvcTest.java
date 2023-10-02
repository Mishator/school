package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private AvatarService avatarService;

    @Test
    public void testCreateStudent() throws Exception {

        Student student = new Student("Ivan", 20);
        Student createdStudent = new Student("Ivan", 20);

        Mockito.when(studentService.add(Mockito.anyString(), Mockito.anyInt())).thenReturn(createdStudent);

        mockMvc.perform(MockMvcRequestBuilders.post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(student)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ivan"))
                .andExpect(jsonPath("$.age").value(20));
    }


    private static String asJsonString(final Object obj) {

            try {
                final ObjectMapper mapper = new ObjectMapper();
                return mapper.writeValueAsString(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    @Test
    public void testGetStudentInfo() throws Exception {
        Student student = new Student("Ivan", 20);
        student.setId(1L);

        Mockito.when(studentService.findStudent(1L)).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/1"))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Ivan")))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.age", Matchers.is(20)));

    }

    @Test
    public void testUpdate() throws Exception {
        Student student = new Student("Ivan", 20);
        student.setId(1L);

        Mockito.when(studentService.update(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt())).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.put("/student/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Ivan\",\"age\":20}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Ivan")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age", Matchers.is(20)));

    }

    @Test
    public void testDelete() throws Exception {
        Student student = new Student("Ivan", 20);
        student.setId(1L);

        Mockito.when(studentService.delete(1L)).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Ivan")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age", Matchers.is(20)));
    }

    @Test
    public void testGetByAge() throws Exception {
        Student student = new Student("Ivan", 20);
        student.setId(1L);


        Mockito.when(studentService.getByAge(20)).thenReturn(Collections.singletonList(student));

        mockMvc.perform(MockMvcRequestBuilders.get("/student/by-age?age=20"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Ivan")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age", Matchers.is(20)));
    }

    @Test
    public void testGetByAgeBetween() throws Exception {
        Student student = new Student("Ivan", 20);
        student.setId(1L);


        Mockito.when(studentService.findByAgeBetween(18, 25)).thenReturn(Collections.singletonList(student));

        mockMvc.perform(MockMvcRequestBuilders.get("/student/by-age-between?min=18&max=25"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Ivan")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age", Matchers.is(20)));
    }

    @Test
    public void testGetFaculty() throws Exception {
        Faculty faculty = new Faculty("Engineering", "Blue");
        faculty.setId(1L);

        Mockito.when(studentService.getFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/faculty-by-id?id=1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Engineering")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color", Matchers.is("Blue")));
    }
}
