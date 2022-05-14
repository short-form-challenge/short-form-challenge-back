package com.leonduri.d7back.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

@Service
public class FileSystemStorageService {

    @Value("${spring.servlet.multipart.location}")
    private String baseUploadPath;


    public void init() {
        try {
            Files.createDirectories(Paths.get(baseUploadPath));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder!");
        }
    }

    private String store(MultipartFile file, String directoryPath, String fileName) {
        String uploadPath = baseUploadPath + directoryPath + "/";
        try {
            if (file.isEmpty()) {
                throw new Exception("ERROR : File is empty.");
            }
            Path root = Paths.get(uploadPath);
            if (!Files.exists(root)) {
                init();
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, root.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        return uploadPath + fileName;
    }

    public String storeProfile(MultipartFile file, String username) {
        String[] tmp = file.getOriginalFilename().split("\\.");
        String newFileName = username + '.' + tmp[tmp.length - 1];
        return store(file, "profiles", newFileName);
    }

    public String storeVideo(MultipartFile video, long videoId) {
        String[] tmp = video.getOriginalFilename().split("\\.");
        String newFileName = "v_" + videoId + '.' + tmp[tmp.length - 1];
        return store(video, "videos", newFileName);
    }

    public String storeThumbnail(MultipartFile thumbnail, long videoId) {
        String[] tmp = thumbnail.getOriginalFilename().split("\\.");
        String newFileName = "t_" + videoId + '.' + tmp[tmp.length - 1];
        return store(thumbnail, "thumbnails", newFileName);
    }


    public Path load(String filename) {
        return Paths.get(baseUploadPath).resolve(filename);
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }

    public void delete(String filePath) {
        FileSystemUtils.deleteRecursively(Paths.get(filePath).toFile());
    }
}