package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student student) {
        Student createdStudent = studentService.add(student.getName(), student.getAge());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> get(@PathVariable Long id) {
        Student student = studentService.get(id);
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable long id, @RequestBody Student student) {
        Student updateStudent = studentService.update(id, student.getName(), student.getAge());
        return ResponseEntity.ok(updateStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> delete(@PathVariable Long id) {
        Student deletedStudent = studentService.delete(id);
        return ResponseEntity.ok(deletedStudent);
    }

    @GetMapping("/by-age")
    public ResponseEntity<List<Student>> getByAge(@RequestParam int age) {
        List<Student> studentsByAge = studentService.getByAge(age);
        return ResponseEntity.ok(studentsByAge);
    }

    @GetMapping("/by-age-between")
    public List<Student> getByAgeBetween(@RequestParam int min, @RequestParam int max) {
        return studentService.findByAgeBetween(min, max);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
