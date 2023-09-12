package ru.hogwarts.school.controller;

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
    public Student create(@RequestBody Student student) {
        return studentService.add(student.getName(),student.getAge());
    }

    @GetMapping
    public Student get(@RequestParam Long id) {
        return studentService.get(id);
    }

    @PutMapping
    public Student update(@RequestBody Student student) {
        return studentService.update(student.getId(), student.getName(),student.getAge());
    }

    @DeleteMapping
    public Student delete(@RequestParam Long id) {
        return studentService.delete(id);
    }

    @GetMapping("/by-age")
    public List<Student> getByAge(@RequestParam int age) {
        return studentService.getByAge(age);
    }
}
