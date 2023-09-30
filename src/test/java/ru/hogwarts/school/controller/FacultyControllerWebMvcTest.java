package ru.hogwarts.school.controller;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

import static java.lang.reflect.Array.get;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    public void testGetFaculty_ExistingFaculty() throws Exception {
        Long facultyId = 1L;
        Faculty faculty = new Faculty(facultyId, "Engineering", "Blue");
        given(facultyService.get(facultyId)).willReturn(faculty);

        mockMvc.perform(get("/faculty/{id}", facultyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(facultyId.intValue())))
                .andExpect(jsonPath("$.name", is(faculty.getName())))
                .andExpect(jsonPath("$.color", is(faculty.getColor())));

        verify(facultyService, times(1)).get(facultyId);
    }

    @Test
    public void testGetFaculty_NonExistingFaculty() throws Exception {
        Long facultyId = 1L;
        given(facultyService.get(facultyId)).willReturn(null);

        mockMvc.perform(get("/faculty/{id}", facultyId))
                .andExpect(status().isNotFound());

        verify(facultyService, times(1)).get(facultyId);
    }

    @Test
    public void testUpdateFaculty() throws Exception {
        Long facultyId = 1L;
        Faculty faculty = new Faculty(facultyId, "Engineering", "Blue");
        Faculty updatedFaculty = new Faculty(facultyId, "Science", "Red");

        given(facultyService.update(facultyId, "Science", "Red")).willReturn(updatedFaculty);

        mockMvc.perform(put("/faculty/{id}", facultyId)
                        .content("{\"name\":\"Science\",\"color\":\"Red\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(facultyId.intValue())))
                .andExpect(jsonPath("$.name", is(updatedFaculty.getName())))
                .andExpect(jsonPath("$.color", is(updatedFaculty.getColor())));

        verify(facultyService, times(1)).update(facultyId, "Science", "Red");
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        Long facultyId = 1L;
        Faculty faculty = new Faculty(facultyId, "Engineering", "Blue");
        given(facultyService.delete(facultyId)).willReturn(faculty);

        mockMvc.perform(delete("/faculty/{id}", facultyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(facultyId.intValue())))
                .andExpect(jsonPath("$.name", is(faculty.getName())))
                .andExpect(jsonPath("$.color", is(faculty.getColor())));

        verify(facultyService, times(1)).delete(facultyId);
    }

    @Test
    public void testGetFacultiesByColor() throws Exception {
        String color = "Blue";
        List<Faculty> faculties = Arrays.asList(
                new Faculty(1L, "Engineering", color),
                new Faculty(2L, "Science", color)
        );

        given(facultyService.getByColor(color)).willReturn(faculties);

        mockMvc.perform(get("/faculty/by-color").param("color", color))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(faculties.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(faculties.get(1).getName())));

        verify(facultyService, times(1)).getByColor(color);
    }

    public void testGetFacultiesByColorOrName() throws Exception {
        String param = "Blue";
        List<Faculty> faculties = Arrays.asList(
                new Faculty(1L, "Engineering", param),
                new Faculty(2L, param, "Red")
        );

        given(facultyService.getByColorOrName(param)).willReturn(faculties);

        mockMvc.perform(get("/faculty/by-color-or-name").param("param", param))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(faculties.get(0).getName())))
                .andExpect(jsonPath("$[1].color", is(faculties.get(1).getColor())));

        verify(facultyService, times(1)).getByColorOrName(param);
    }

    @Test
    public void testGetStudentsByFacultyId() throws Exception {
        long facultyId = 1L;
        List<Student> students = Arrays.asList(
                new Student(1L, "Ivan", 20),
                new Student(2L, "Petr", 22)
        );

        given(facultyService.getStudents(facultyId)).willReturn(students);

        mockMvc.perform(get("/faculty/students-by-id").param("id", String.valueOf(facultyId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(students.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(students.get(1).getName())));

        verify(facultyService, times(1)).getStudents(facultyId);
    }


}
