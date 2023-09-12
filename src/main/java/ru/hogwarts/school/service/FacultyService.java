package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }


    public Faculty add(String name, String color) {
        Faculty newFaculty = new Faculty(name, color);
        return facultyRepository.save(newFaculty);
    }

    public Faculty get(long id) {
        return facultyRepository.findById(id).get();
    }

    public Faculty update(long id, String name, String color) {
        Faculty facultyForUpdate = facultyRepository.findById(id).get();
        facultyForUpdate.setName(name);
        facultyForUpdate.setColor(color);
        return facultyRepository.save(facultyForUpdate);
    }

    public Faculty delete(long id) {
        Faculty facultyForDelete = facultyRepository.findById(id).get();
        facultyRepository.deleteById(id);
        return facultyForDelete;
    }

    public List<Faculty> getByColor(String color) {
        return facultyRepository.findAllByColor(color);
    }
}
