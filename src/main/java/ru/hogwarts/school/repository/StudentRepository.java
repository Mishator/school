package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.entity.Student;

import java.util.List;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllByAge(Integer age);

    List<Student> findByAgeBetween(Integer min, Integer max);

    List<Student> findByFacultyId(Long id);
}
