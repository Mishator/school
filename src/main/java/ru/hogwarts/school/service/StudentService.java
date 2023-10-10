package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);


    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student add(String name, int age) {
        logger.info("Был вызван метод add");
        Student newStudent = new Student(name, age);
        return studentRepository.save(newStudent);
    }

    public Student findStudent(long id) {
        logger.info("Был вызван метод findStudent");
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            return studentOptional.get();
        } else {
            throw new IllegalArgumentException("Факультет с id " + id + " не найден");
        }
    }

    public Student update(long id, String name, int age) {
        logger.info("Был вызван метод update");
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
        logger.info("Был вызван метод delete");
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
        logger.info("Был вызван метод getByAge");
        return studentRepository.findAllByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        logger.info("Был вызван метод findByAgeBetween");
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFaculty(Long studentId) {
        logger.info("Был вызван метод getFaculty");
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElse(null);
    }

    public List<Student> findByFacultyId(long facultyId) {
        logger.info("Был вызван метод findByFacultyId");
        return studentRepository.findByFacultyId(facultyId);
    }

    public Integer count() {
        logger.info("Был вызван метод count");
        return studentRepository.countStudent();
    }

    public double findAvgAge() {
        logger.info("Был вызван метод findAvgAge");
        return studentRepository.findAvgAge();
    }

    public List<Student> findLastFiveStudents() {
        logger.info("Был вызван метод findLastFiveStudents");
        return studentRepository.findLastStudents(5);
    }


}
