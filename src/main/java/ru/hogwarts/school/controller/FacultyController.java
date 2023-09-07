package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty create(@RequestBody Faculty faculty) {
        return facultyService.add(faculty.getName(), faculty.getColor());
    }

    @GetMapping
    public Faculty get(@RequestParam Long id) {
        return facultyService.get(id);
    }

    @PutMapping
    public Faculty update(@RequestBody Faculty faculty) {
        return facultyService.update(faculty.getId(), faculty.getName(), faculty.getColor());
    }

    @DeleteMapping
    public Faculty delete(@RequestParam Long id) {
        return facultyService.delete(id);
    }

    @GetMapping("/by-color")
    public List<Faculty> getByColor(@RequestParam String color) {
        return facultyService.getByColor(color);
    }

}
