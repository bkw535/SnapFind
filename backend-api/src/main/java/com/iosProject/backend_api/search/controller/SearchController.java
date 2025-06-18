package com.iosProject.backend_api.search.controller;

import com.iosProject.backend_api.search.dto.SearchResultResponse;
import com.iosProject.backend_api.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<SearchResultResponse> searchFromImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId
    ) {
        SearchResultResponse response = searchService.processSearch(file, userId);
        System.out.println("파일명: " + file.getOriginalFilename() + ", userId: " + userId);
        return ResponseEntity.ok(response);
    }
}
