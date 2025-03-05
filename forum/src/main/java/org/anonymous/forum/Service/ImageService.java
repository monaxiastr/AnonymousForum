package org.anonymous.forum.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageService {
    public String uploadImage(MultipartFile file) {
        // 使用 UUID 生成唯一的文件名
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get("uploadedImg").resolve(fileName);
        File dir = new File(path.toString());
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.err.println("Could not create directory: " + dir);
                return null;
            }
        }
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return null;
        }
        // 构建图片 URL
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploadedImg/")
                .path(fileName)
                .toUriString();
    }
}
