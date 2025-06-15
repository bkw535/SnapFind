package com.iosProject.backend_api.search.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SearchHistory searchHistory;

    private String name;
    private String price;
    private String shopUrl;
    private String shop;
}
