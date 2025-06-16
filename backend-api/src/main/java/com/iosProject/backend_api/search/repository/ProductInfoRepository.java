package com.iosProject.backend_api.search.repository;

import com.iosProject.backend_api.search.domain.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {
}
