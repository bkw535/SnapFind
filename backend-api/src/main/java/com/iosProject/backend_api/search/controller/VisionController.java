package com.iosProject.backend_api.search.controller;

import com.iosProject.backend_api.search.service.VisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class VisionController {

    private final VisionService visionService;

    @PostMapping(value = "/vision", consumes = "multipart/form-data")
    public ResponseEntity<List<String>> analyzeImage(@RequestParam("file") MultipartFile file) throws Exception {
        byte[] imageBytes = file.getBytes();
        List<String> labels = visionService.detectLabels(imageBytes);
        return ResponseEntity.ok(labels);
    }
}
