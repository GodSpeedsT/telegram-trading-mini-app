package com.app.tradeguess;

import com.app.tradeguess.service.BinanceDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
@EnableCaching
@EnableScheduling
@RequiredArgsConstructor
public class TradeGuessApplication implements CommandLineRunner {

    private final BinanceDataService binanceDataService;
    public static void main(String[] args) {

        SpringApplication.run(TradeGuessApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length > 0 && "--load-data".equals(args[0])) {
            log.info("Запуск загрузки данных по команде...");
            binanceDataService.loadTestData();
            log.info("Загрузка данных завершена. Приложение продолжает работу...");
        }
    }

}


