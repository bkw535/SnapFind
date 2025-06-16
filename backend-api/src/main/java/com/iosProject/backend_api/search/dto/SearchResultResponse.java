package com.iosProject.backend_api.search.dto;

import com.iosProject.backend_api.search.domain.ProductInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class SearchResultResponse {
    private String keyword;
    private List<ProductDto> products;

    public static SearchResultResponse of(String keyword, List<ProductInfo> productInfos) {
        List<ProductDto> products = productInfos.stream()
                .map(ProductDto::fromEntity)
                .collect(Collectors.toList());
        return new SearchResultResponse(keyword, products);
    }
}
