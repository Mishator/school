package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.entity.Student;

import java.util.List;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllByAge(Integer age);

    List<Student> findByAgeBetween(Integer min, Integer max);

    List<Student> findByFacultyId(Long id);

    @Query(value = "select count(*) from student s", nativeQuery = true)
    Integer countStudent();

    @Query(value = "select avg(age) from student", nativeQuery = true)
    Double findAvgAge();

    @Query(value = "select * from student order by id desc limit :size", nativeQuery = true)
    List<Student> findLastStudents(int size);
}
