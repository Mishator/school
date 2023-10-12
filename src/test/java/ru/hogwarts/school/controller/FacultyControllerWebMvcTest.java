package ru.hogwarts.school.controller;

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
import ru.hogwarts.school.service.FacultyService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Test
    public void testCreateFaculty() throws Exception {
        // Тестирование создания факультета
        Faculty faculty = new Faculty("Engineering", "Blue");
        faculty.setName("Engineering");
        faculty.setColor("Blue");

        Faculty createdFaculty = new Faculty( "Engineering", "Blue");
        createdFaculty.setId(1L);
        createdFaculty.setName("Engineering");
        createdFaculty.setColor("Blue");

        when(facultyService.add(anyString(), anyString())).thenReturn(createdFaculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"TestFaculty\",\"color\":\"TestColor\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Engineering"))
                .andExpect(jsonPath("$.color").value("Blue"));
    }

    @Test
    public void testGet() throws Exception {
        Faculty faculty = new Faculty("Engineering", "Blue");
        faculty.setId(1L);

        Mockito.when(facultyService.get(1L)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Engineering")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color", Matchers.is("Blue")));
    }

    @Test
    public void testUpdate() throws Exception {
        Faculty faculty = new Faculty("Engineering", "Blue");
        faculty.setId(1L);

        Mockito.when(facultyService.update(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString())).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.put("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Engineering\",\"color\":\"Blue\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Engineering")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color", Matchers.is("Blue")));
    }

    @Test
    public void testDelete() throws Exception {
        Faculty faculty = new Faculty("Engineering", "Blue");
        faculty.setId(1L);

        Mockito.when(facultyService.delete(1L)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.delete("/faculty/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Engineering")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color", Matchers.is("Blue")));
    }

    @Test
    public void testGetFacultiesByColor() throws Exception {
        Faculty faculty = new Faculty("Engineering", "Blue");
        faculty.setId(1L);

        Mockito.when(facultyService.getByColor("Blue")).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/by-color?color=Blue"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Engineering")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].color", Matchers.is("Blue")));
    }

    @Test
    public void testGetFacultiesByColorOrName() throws Exception {
        Faculty expectedFaculties = new Faculty("Engineering", "Blue");


        Mockito.when(facultyService.getByColorOrName(Mockito.anyString())).thenReturn(Collections.singletonList(expectedFaculties));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/by-color-or-name")
                        .param("param", "Engineering"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Engineering"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].color").value("Blue"));

    }

    @Test
    public void testGetStudentsById() throws Exception {
        long id = 1;
        List<Student> students = new ArrayList<>(
                Collections.singleton(new Student("Ivan", 20)));

        when(facultyService.getStudents(id)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/students-by-id")
                        .param("id", String.valueOf(id)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(students.size())));
    }
}
