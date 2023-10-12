package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final Object object = new Object();


    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student add(String name, int age) {
        //    logger.info("Был вызван метод add");
        Student newStudent = new Student(name, age);
        return studentRepository.save(newStudent);
    }

    public Student findStudent(long id) {
        //    logger.info("Был вызван метод findStudent");
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            return studentOptional.get();
        } else {
            throw new IllegalArgumentException("Факультет с id " + id + " не найден");
        }
    }

    public Student update(long id, String name, int age) {
        //    logger.info("Был вызван метод update");
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
        //    logger.info("Был вызван метод delete");
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
        //    logger.info("Был вызван метод getByAge");
        return studentRepository.findAllByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        //    logger.info("Был вызван метод findByAgeBetween");
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFaculty(Long studentId) {
        //    logger.info("Был вызван метод getFaculty");
        return studentRepository.findById(studentId)
                .map(Student::getFaculty)
                .orElse(null);
    }

    public List<Student> findByFacultyId(long facultyId) {
        //    logger.info("Был вызван метод findByFacultyId");
        return studentRepository.findByFacultyId(facultyId);
    }

    public Integer count() {
        //    logger.info("Был вызван метод count");
        return studentRepository.countStudent();
    }

    public double findAvgAge() {
        //    logger.info("Был вызван метод findAvgAge");
        return studentRepository.findAvgAge();
    }

    public List<Student> findLastFiveStudents() {
        //    logger.info("Был вызван метод findLastFiveStudents");
        return studentRepository.findLastStudents(5);
    }

    public List<String> findAllStartFromA() {
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(name -> name.startsWith("А"))
                .sorted()
                .collect(Collectors.toList());
    }

    public double avgAge() {
        return studentRepository.findAll().stream()
                .mapToDouble(i -> (double) i.getAge())
                .average()
                .orElseThrow(() -> new RuntimeException("Ошибка вычисления среднего возраста"));
    }

    public int calculate() {
        long start = System.currentTimeMillis();
        int result = Stream
                .iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, (a, b) -> a + b);
        long finish = System.currentTimeMillis();
        logger.info("Calculate time: " + (finish - start));
        return result;
    }

    /*
       2023-10-11 23:42:33.205  INFO 16576 --- [nio-8081-exec-8] r.h.school.service.StudentService

       без parallel
       Calculate time: 40
       Calculate time: 29
       Calculate time: 27
       Calculate time: 31
       Calculate time: 23

       c parallel
       Calculate time: 88
       Calculate time: 32
       Calculate time: 31
       Calculate time: 35
       Calculate time: 33


    * */

    public void printStudentsName() {
        List<Student> students = studentRepository.findAll();
        printStudentName(students.get(0));
        printStudentName(students.get(1));

        new Thread(() -> {
            printStudentName(students.get(2));
            printStudentName(students.get(3));
        }).start();

        new Thread(() -> {
            printStudentName(students.get(4));
            printStudentName(students.get(5));
        }).start();

    }


    private void printStudentName(Student student) {
        System.out.println(Thread.currentThread() + " " + student);
    }

    public void printStudentsNameSync() {
        List<Student> students = studentRepository.findAll();
        printStudentNameSync(students.get(0));
        printStudentNameSync(students.get(1));

        new Thread(() -> {
            printStudentNameSync(students.get(2));
            printStudentNameSync(students.get(3));
        }).start();

        new Thread(() -> {
            printStudentNameSync(students.get(4));
            printStudentNameSync(students.get(5));
        }).start();

    }

    private void printStudentNameSync(Student student) {
        synchronized (object) {
            System.out.println(Thread.currentThread() + " " + student);
        }

    }


}
