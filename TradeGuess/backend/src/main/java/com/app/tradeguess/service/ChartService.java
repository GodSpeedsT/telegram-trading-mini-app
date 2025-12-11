package com.app.tradeguess.service;

import com.app.tradeguess.model.entity.ChartSegment;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class ChartService {
    public ChartSegment getRandomSegment() {
        // Простейшая реализация
        Long count = chartRepository.count();
        Long randomId = ThreadLocalRandom.current().nextLong(1, count + 1);
        return chartRepository.findById(randomId).orElseThrow();
    }
}