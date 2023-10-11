package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    private final StudentService studentService;

  //  private final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository, StudentService studentService) {
        this.facultyRepository = facultyRepository;
        this.studentService = studentService;
    }


    public Faculty add(String name, String color) {
    //    logger.info("Был вызван метод add");
        Faculty newFaculty = new Faculty(name, color);
        return facultyRepository.save(newFaculty);
    }

    public Faculty get(long id) {
    //    logger.info("Был вызван метод get");
        Optional<Faculty> facultyOptional = facultyRepository.findById(id);
        if (facultyOptional.isPresent()) {
            return facultyOptional.get();
        } else {
            throw new IllegalArgumentException("Факультет с id " + id + " не найден");
        }
    }

    public Faculty update(long id, String name, String color) {
    //    logger.info("Был вызван метод update");
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
    //    logger.info("Был вызван метод delete");
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
    //    logger.info("Был вызван метод getByColor");
        return facultyRepository.findAllByColor(color);
    }

    public List<Faculty> getByColorOrName(String param) {
    //    logger.info("Был вызван метод getByColorOrName");
        return facultyRepository.findByColorContainsIgnoreCaseOrNameContainsIgnoreCase(param, param);
    }

    public List<Student> getStudents(Long id) {
    //    logger.info("Был вызван метод getStudents");
        return studentService.findByFacultyId(id);
    }

}
