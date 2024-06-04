package com.ahia.blog.controller;


import com.ahia.blog.entity.R;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
public class ImgController {
    private static final String UPLOADED_FOLDER = "./uploads/";
    private final ResourceLoader resourceLoader;

    public ImgController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostMapping("/api/uploads/img")
    public R uploads(@RequestParam("file") MultipartFile[] file){
        List<String> list = new ArrayList<>();
        for (MultipartFile f : file) {
            try {
                byte[] bytes = f.getBytes();
                String originalFilename = f.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                String newFilename = UUID.randomUUID() + extension;
                Path path = Paths.get(UPLOADED_FOLDER+"img/" + newFilename);
                Files.write(path, bytes);
                list.add("/api/image/" + newFilename);
            } catch (IOException e) {
                e.printStackTrace();
                R.error("上传失败");
            }
        }
        return R.ok("上传成功",list);
    }
    @PostMapping("/api/upload/img")
    public R upload(MultipartFile file){
        try {
            byte[] bytes = file.getBytes();
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String newFilename = UUID.randomUUID() + extension;
            Path path = Paths.get(UPLOADED_FOLDER+"img/" + newFilename);
            Files.write(path, bytes);
            Map<String, String> data = new HashMap<>();
            data.put("url", "/api/img/" + newFilename);
            return R.ok("上传成功",data).put("errno",0);
        }catch(IOException e) {
            e.printStackTrace();
            return R.error("上传失败");
        }
    }

    @GetMapping("/api/img/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Resource file = resourceLoader.getResource("file:" + Paths.get(UPLOADED_FOLDER+"img/", filename));
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
    }
    @GetMapping("/api/avatar/{filename:.+}")
    public ResponseEntity<Resource> getAvatar(@PathVariable String filename) {
        Resource file = resourceLoader.getResource("file:" + Paths.get(UPLOADED_FOLDER+"avatar/", filename));
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
    }
}
