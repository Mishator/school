package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;
import java.util.Optional;

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
        Optional<Faculty> facultyOptional = facultyRepository.findById(id);
        if (facultyOptional.isPresent()) {
            return facultyOptional.get();
        } else {
            throw new IllegalArgumentException("Факультет с id " + id + " не найден");
        }
    }

    public Faculty update(long id, String name, String color) {
        Optional<Faculty> facultyOptional = facultyRepository.findById(id);
        if (facultyOptional.isPresent()) {
            Faculty facultyForUpdate = facultyOptional.get();
            facultyForUpdate.setName(name);
            facultyForUpdate.setColor(color);
            return facultyRepository.save(facultyForUpdate);
        } else {
            throw new IllegalArgumentException("Факультет с id " + id + " не найден");
        }
    }

    public Faculty delete(long id) {
        Optional<Faculty> facultyOptional = facultyRepository.findById(id);
        if (facultyOptional.isPresent()) {
            Faculty facultyForDelete = facultyOptional.get();
            facultyRepository.deleteById(id);
            return facultyForDelete;
        } else {
            throw new IllegalArgumentException("Факультет с id " + id + " не найден");
        }
    }

    public List<Faculty> getByColor(String color) {
        return facultyRepository.findAllByColor(color);
    }
}
