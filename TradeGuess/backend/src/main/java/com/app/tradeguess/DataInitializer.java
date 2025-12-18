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
            long segmentCount = chartSegmentRepository.count();
            log.info("Current segment count in DB: {}", segmentCount);

            if (segmentCount < 100) {
                log.info("Starting data initialization...");
                try {
                    binanceDataService.loadTestData();
                    log.info("Data initialization completed successfully");
                } catch (Exception e) {
                    log.warn("Failed to load data from Binance (likely geo-restriction). Continuing without historical data. Error: {}",
                            e.getMessage());
                    // Продолжаем работу с пустой/частичной базой
                }
            } else {
                log.info("DB already has {} segments. Skipping data load.", segmentCount);
            }
        } catch (Exception e) {
            log.error("Unexpected error in DataInitializer: {}", e.getMessage(), e);
            // НЕ выбрасываем исключение - приложение должно запуститься
        }
    }
}