package com.app.tradeguess.service;

import com.app.tradeguess.model.entity.ChartSegment;
import com.app.tradeguess.repository.ChartSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ChartService {

    private final ChartSegmentRepository chartRepository;

    public ChartSegment getRandomSegment() {
        Long count = chartRepository.count();
        Long randomId = ThreadLocalRandom.current().nextLong(1, count + 1);
        return chartRepository.findById(randomId).orElseThrow();
    }
}