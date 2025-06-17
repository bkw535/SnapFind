package com.iosProject.backend_api.user.service;

import com.iosProject.backend_api.user.repository.UserHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.iosProject.backend_api.search.domain.SearchHistory;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserHistoryService {

    private final UserHistoryRepository userHistoryRepository;

    public List<String> getSearchHistory(Long userId) {
        return userHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(SearchHistory::getProduct)
                .collect(Collectors.toList());
    }
}
