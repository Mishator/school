package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static java.lang.reflect.Array.get;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
        Long studentId = 1L;
        Student student = new Student(studentId, "Ivan", 20);

        given(studentService.findStudent(studentId)).willReturn(student);

        mockMvc.perform(get("/student/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(studentId.intValue())))
                .andExpect(jsonPath("$.name", is(student.getName())))
                .andExpect(jsonPath("$.age", is(student.getAge())));

        verify(studentService, times(1)).findStudent(studentId);

    }

    @Test
    public void testGetStudentInfo_NonExistingStudent() throws Exception {
        Long studentId = 1L;
        given(studentService.findStudent(studentId)).willReturn(null);

        mockMvc.perform(get("/student/{id}", studentId))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).findStudent(studentId);

    }

    @Test
    public void testUpdateStudent() throws Exception {
        Long studentId = 1L;
        Student student = new Student(studentId, "Ivan", 20);
        Student updatedStudent = new Student(studentId, "Ivan", 21);

        given(studentService.update(studentId, "Ivan", 21)).willReturn(updatedStudent);

        mockMvc.perform(put("/student/{id}", studentId)
                        .content("{\"name\":\"Jane Doe\",\"age\":21}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(studentId.intValue())))
                .andExpect(jsonPath("$.name", is(updatedStudent.getName())))
                .andExpect(jsonPath("$.age", is(updatedStudent.getAge())));

        verify(studentService, times(1)).update(studentId, "Ivan", 21);
    }

    @Test
    public void testDeleteStudent() throws Exception {
        Long studentId = 1L;
        Student student = new Student(studentId, "Ivan", 20);
        given(studentService.delete(studentId)).willReturn(student);

        mockMvc.perform(delete("/student/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(studentId.intValue())))
                .andExpect(jsonPath("$.name", is(student.getName())))
                .andExpect(jsonPath("$.age", is(student.getAge())));

        verify(studentService, times(1)).delete(studentId);
    }

    @Test
    public void testGetStudentsByAge() throws Exception {
        int age = 20;
        List<Student> students = Arrays.asList(
                new Student(1L, "Ivan", age),
                new Student(2L, "Petr", age)
        );

        given(studentService.getByAge(age)).willReturn(students);

        mockMvc.perform(get("/student/by-age").param("age", String.valueOf(age)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(students.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(students.get(1).getName())));

        verify(studentService, times(1)).getByAge(age);
    }

    @Test
    public void testGetStudentsByAgeBetween() throws Exception {
        int minAge = 18;
        int maxAge = 25;
        List<Student> students = Arrays.asList(
                new Student(1L, "Ivan", 20),
                new Student(2L, "Petr", 22)
        );

        given(studentService.findByAgeBetween(minAge, maxAge)).willReturn(students);

        mockMvc.perform(get("/student/by-age-between")
                        .param("min", String.valueOf(minAge))
                        .param("max", String.valueOf(maxAge)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(students.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(students.get(1).getName())));

        verify(studentService, times(1)).findByAgeBetween(minAge, maxAge);
    }

    @Test
    public void testGetFaculty() throws Exception {
        long facultyId = 1L;
        Faculty faculty = new Faculty(facultyId, "Engineering", "Blue");
        given(studentService.getFaculty(facultyId)).willReturn(faculty);

        mockMvc.perform(get("/student/faculty-by-id")
                        .param("id", String.valueOf(facultyId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(facultyId.intValue())))
                .andExpect(jsonPath("$.name", is(faculty.getName())))
                .andExpect(jsonPath("$.color", is(faculty.getColor())));

        verify(studentService, times(1)).getFaculty(facultyId);
    }



}
