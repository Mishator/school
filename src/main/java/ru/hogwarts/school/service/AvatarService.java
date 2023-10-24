package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarService {

    private final String avatarsDir;
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;
    private Student student;

  //  private final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    public AvatarService(
            @Value("${path.to.avatars.folder}")
            String avatarsDir,
            StudentRepository studentRepository,
            AvatarRepository avatarRepository
    ) {
        this.avatarsDir = avatarsDir;
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Avatar findAvatar(long studentId) {
    //    logger.info("Был вызван метод findAvatar");
        return avatarRepository.findByStudentId(studentId).orElseThrow();
    }

    public void uploadAvatar(Long facultyId, MultipartFile avatarFile) throws IOException {
    //    logger.info("Был вызван метод uploadAvatar");
        Student student = studentRepository.findById(facultyId).orElse(null);

        Path filePath = Path.of(avatarsDir, student + "." + getExtension(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = avatarFile.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {

            bis.transferTo(bos);

        }

        Avatar avatar = new Avatar();
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());

        avatarRepository.save(avatar);

    }

    private Avatar getByStudentId(Long studentId) {
    //    logger.info("Был вызван метод getByStudentId");
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    private String getExtension(String fileName) {
    //    logger.info("Был вызван метод getExtension");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public StudentRepository getStudentRepository() {
        return studentRepository;
    }

    public List<Avatar> getPage(int pageNo, int pageSize) {
        PageRequest page = PageRequest.of(pageNo, pageSize);
        return avatarRepository.findAll(page).getContent();
    }
}
