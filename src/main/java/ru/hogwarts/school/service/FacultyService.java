package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private static long COUNTER = 0;

    private final Map<Long, Faculty> facultyById = new HashMap<>();

    public Faculty add(String name, String color) {
        Faculty newFaculty = new Faculty(++COUNTER, name, color);
        facultyById.put(newFaculty.getId(), newFaculty);
        return newFaculty;
    }

    public Faculty get(long id) {
        return facultyById.get(id);
    }

    public Faculty update(long id, String name, String color) {
        Faculty facultyForUpdate = facultyById.get(id);
        facultyForUpdate.setName(name);
        facultyForUpdate.setColor(color);
        return facultyForUpdate;
    }

    public Faculty delete(long id) {
        Faculty facultyForDelete = facultyById.get(id);
        facultyById.remove(id);
        return facultyForDelete;
    }

    public List<Faculty> getByColor(String color) {
        return facultyById.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
    }
}
