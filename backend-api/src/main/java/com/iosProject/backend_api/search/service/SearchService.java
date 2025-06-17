package com.iosProject.backend_api.search.service;

import com.iosProject.backend_api.search.domain.ProductInfo;
import com.iosProject.backend_api.search.domain.SearchHistory;
import com.iosProject.backend_api.search.dto.ProductDto;
import com.iosProject.backend_api.search.dto.SearchResultResponse;
import com.iosProject.backend_api.search.repository.ProductInfoRepository;
import com.iosProject.backend_api.search.repository.SearchHistoryRepository;
import com.iosProject.backend_api.user.domain.User;
import com.iosProject.backend_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final VisionService visionService;
    private final CrawlingService crawlingService;
    private final SearchHistoryRepository searchHistoryRepository;
    private final ProductInfoRepository productInfoRepository;
    private final UserRepository userRepository;

    public SearchResultResponse processSearch(MultipartFile file, Long userId) {
        List<String> labels = visionService.extractLabels(file);
        log.info("추출된 라벨 목록: {}", labels);

        List<String> topLabels = labels.stream()
                .limit(2)
                .toList();

        log.info("사용된 검색 키워드들: {}", topLabels);

        Set<ProductInfo> productSet = new java.util.HashSet<>();
        for (String keyword : topLabels) {
            List<ProductInfo> results = crawlingService.searchProducts(keyword);
            productSet.addAll(results);
            log.info("[{}] 키워드로 검색된 상품 수: {}", keyword, results.size());
        }

        log.info("통합 검색 결과 수: {}", productSet.size());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        String primaryKeyword = topLabels.get(0);
        SearchHistory history = SearchHistory.builder()
                .user(user)
                .product(primaryKeyword)
                .build();
        searchHistoryRepository.save(history);

        for (ProductInfo product : productSet) {
            product.setSearchHistory(history);
        }
        productInfoRepository.saveAll(productSet);

        List<ProductDto> resultDtos = productSet.stream()
                .map(ProductDto::fromEntity)
                .toList();

        return new SearchResultResponse(primaryKeyword, resultDtos);
    }
}