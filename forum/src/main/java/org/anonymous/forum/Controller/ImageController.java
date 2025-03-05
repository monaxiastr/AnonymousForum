package org.anonymous.forum.Controller;

import org.anonymous.forum.Service.ImageService;
import org.anonymous.forum.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ImageController {
    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @PostMapping("/img/uploadAvatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file, @RequestParam("id") String id) {
        if (file != null && !file.isEmpty()) {
            String imgUrl = imageService.uploadImage(file);
            if (imgUrl == null || imgUrl.isEmpty()) {
                return ResponseEntity.status(500).body("文件上传失败");
            }
            if (userService.setAvatarUrl(id, imgUrl)) {
                return ResponseEntity.ok(imgUrl);
            } else {
                return ResponseEntity.status(500).body("未找到用户！");
            }
        } else {
            return ResponseEntity.badRequest().body("未上传任何文件！");
        }
    }

    @GetMapping("/uploadedImg/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get("uploadedImg").resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                MediaType mediaType = MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
