package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.entity.Faculty;
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
    public ResponseEntity<Faculty> create(@RequestBody Faculty faculty) {
        Faculty createdFaculty = facultyService.add(faculty.getName(), faculty.getColor());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFaculty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> get(@PathVariable Long id) {
        Faculty faculty = facultyService.get(id);
        return ResponseEntity.ok(faculty);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faculty> update(@PathVariable Long id, @RequestBody Faculty faculty) {
        Faculty updateFaculty = facultyService.update(id, faculty.getName(), faculty.getColor());
        return ResponseEntity.ok(updateFaculty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Faculty> delete(@PathVariable Long id) {
        Faculty deleteFaculty = facultyService.delete(id);
        return ResponseEntity.ok(deleteFaculty);
    }

    @GetMapping("/by-color")
    public ResponseEntity<List<Faculty>> getByColor(@RequestParam String color) {
        List<Faculty> facultiesByColor = facultyService.getByColor(color);
        return ResponseEntity.ok(facultiesByColor);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
