package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student add(String name, int age) {
        Student newStudent = new Student(name, age);
        return studentRepository.save(newStudent);
    }

    public Student get(long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            return studentOptional.get();
        } else {
            throw new IllegalArgumentException("Факультет с id " + id + " не найден");
        }
    }

    public Student update(long id, String name, int age) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            Student studentForUpdate = studentOptional.get();
            studentForUpdate.setName(name);
            studentForUpdate.setAge(age);
            return studentRepository.save(studentForUpdate);
        } else {
            throw new IllegalArgumentException("Факультет с id " + id + " не найден");
        }
    }

    public Student delete(long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            Student studentForDelete = studentOptional.get();
            studentRepository.deleteById(id);
            return studentForDelete;
        } else {
            throw new IllegalArgumentException("Факультет с id " + id + " не найден");
        }
    }

    public List<Student> getByAge(int age) {
        return studentRepository.findAllByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

}
