package com.iosProject.backend_api.search.dto;


import com.iosProject.backend_api.search.domain.ProductInfo;
import lombok.Data;

@Data
public class ProductDto {
    private String name;
    private String price;
    private String shopUrl;
    private String shop;

    public static ProductDto fromEntity(ProductInfo entity) {
        ProductDto dto = new ProductDto();
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setShopUrl(entity.getShopUrl());
        dto.setShop(entity.getShop());
        return dto;
    }
}
