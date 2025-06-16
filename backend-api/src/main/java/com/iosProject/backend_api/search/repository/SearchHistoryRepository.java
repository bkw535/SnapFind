package com.iosProject.backend_api.search.repository;

import com.iosProject.backend_api.search.domain.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
}
