package com.iosProject.backend_api.user.repository;

import com.iosProject.backend_api.search.domain.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
