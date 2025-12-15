package com.app.tradeguess;

import com.app.tradeguess.repository.ChartSegmentRepository;
import com.app.tradeguess.service.BinanceDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final BinanceDataService binanceDataService;
    private final ChartSegmentRepository chartSegmentRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            if (chartSegmentRepository.count() < 1000) {
                log.info("Starting data initialization...");
                binanceDataService.loadTestData();
            } else {
                log.info("В бд уже есть {} сегментов. Пропускаем загрузку.",
                        chartSegmentRepository.count());
            }
        } catch (Exception e) {
            log.error("Failed to load data, binance service send error", e.getMessage());
        }
    }
}
