package com.app.tradeguess.service;

import com.app.tradeguess.model.entity.ChartSegment;
import com.app.tradeguess.repository.ChartSegmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChartService {

    private final ChartSegmentRepository chartRepository;

    @Cacheable(value = "chartSegment", key = "'random'")
    public ChartSegment getRandomSegment() {
        log.info("Getting random segment from DB");
        return chartRepository.findRandom()
                .orElseThrow(() -> new RuntimeException("Нет доступных сегментов"));
    }

    @Cacheable(value = "chartSegment", key = "#segmentId")
    public ChartSegment getSegmentById(Long segmentId) {
        log.info("Getting segment {} from DB", segmentId);
        return chartRepository.findById(segmentId)
                .orElseThrow(() -> new RuntimeException("Сегмент не найден"));
    }
}