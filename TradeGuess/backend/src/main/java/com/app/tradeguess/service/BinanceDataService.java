package com.app.tradeguess.service;

import com.app.tradeguess.model.entity.ChartSegment;
import com.app.tradeguess.repository.ChartSegmentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinanceDataService {

    private final ChartSegmentRepository chartSegmentRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public void loadHistoricalData(String symbol, String interval, int limit) {
        log.info("Загрузка исторических данных для {}/{} (лимит: {})", symbol, interval, limit);

        try {
            String url = String.format(
                    "https://api.binance.com/api/v3/klines?symbol=%s&interval=%s&limit=%d",
                    symbol, interval, limit
            );

            log.debug("Запрос к Binance API: {}", url);
            String response = restTemplate.getForObject(url, String.class);

            List<ChartSegment> segments = parseKlineData(response, symbol, interval);
            chartSegmentRepository.saveAll(segments);

            log.info("Успешно сохранено {} сегментов для {}", segments.size(), symbol);

        } catch (Exception e) {
            log.error("Ошибка загрузки данных для {}: {}", symbol, e.getMessage(), e);
            throw new RuntimeException("Ошибка загрузки данных Binance", e);
        }
    }

    private List<ChartSegment> parseKlineData(String jsonResponse, String symbol, String timeframe) {
        List<ChartSegment> segments = new ArrayList<>();

        try {
            List<List<Object>> klines = objectMapper.readValue(
                    jsonResponse,
                    new TypeReference<List<List<Object>>>() {}
            );

            for (int i = 0; i < klines.size() - 80; i += 10) { // Шаг 10 для разнообразия

                List<List<Object>> displayKlines = klines.subList(i, i + 70);
                String displayCandlesJson = convertKlinesToJson(displayKlines);

                List<List<Object>> resultKlines = klines.subList(i + 70, i + 80);
                String resultCandlesJson = convertKlinesToJson(resultKlines);

                BigDecimal priceAtDecision = extractClosePrice(displayKlines.get(displayKlines.size() - 1));
                BigDecimal priceAtTarget = extractClosePrice(resultKlines.get(resultKlines.size() - 1));

                int calculatedDirection = priceAtTarget.compareTo(priceAtDecision) > 0 ? 1 : -1;

                ChartSegment segment = new ChartSegment();
                segment.setDisplayCandles(displayCandlesJson);
                segment.setResultCandles(resultCandlesJson);
                segment.setPriceAtDecision(priceAtDecision);
                segment.setPriceAtTarget(priceAtTarget);
                segment.setCalculatedDirection(calculatedDirection);

                segments.add(segment);

                log.debug("Создан сегмент: {} → {} ({}%)",
                        priceAtDecision.setScale(2, RoundingMode.HALF_UP),
                        priceAtTarget.setScale(2, RoundingMode.HALF_UP),
                        priceAtTarget.subtract(priceAtDecision)
                                .divide(priceAtDecision, 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100))
                                .setScale(2, RoundingMode.HALF_UP)
                );
            }

        } catch (Exception e) {
            log.error("Ошибка парсинга данных Binance: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка парсинга данных Binance", e);
        }

        return segments;
    }

    private String convertKlinesToJson(List<List<Object>> klines) {
        List<Map<String, Object>> candles = new ArrayList<>();

        for (List<Object> kline : klines) {
            Map<String, Object> candle = new HashMap<>();

            long timestamp = Long.parseLong(kline.get(0).toString());
            BigDecimal open = new BigDecimal(kline.get(1).toString());
            BigDecimal high = new BigDecimal(kline.get(2).toString());
            BigDecimal low = new BigDecimal(kline.get(3).toString());
            BigDecimal close = new BigDecimal(kline.get(4).toString());
            BigDecimal volume = new BigDecimal(kline.get(5).toString());

            candle.put("t", timestamp);
            candle.put("o", open.doubleValue());
            candle.put("h", high.doubleValue());
            candle.put("l", low.doubleValue());
            candle.put("c", close.doubleValue());
            candle.put("v", volume.doubleValue());

            candles.add(candle);
        }

        try {
            return objectMapper.writeValueAsString(candles);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка конвертации в JSON", e);
        }
    }

    private BigDecimal extractClosePrice(List<Object> kline) {
        return new BigDecimal(kline.get(4).toString());
    }

    public void loadMultipleSymbols() {
        List<String> symbols = List.of(
                "BTCUSDT", "ETHUSDT", "BNBUSDT", "ADAUSDT", "SOLUSDT",
                "XRPUSDT", "DOTUSDT", "DOGEUSDT", "AVAXUSDT", "MATICUSDT"
        );

        for (String symbol : symbols) {
            try {
                loadHistoricalData(symbol, "5m", 500);
                Thread.sleep(1000);
            } catch (Exception e) {
                log.error("Ошибка загрузки {}: {}", symbol, e.getMessage());
            }
        }
    }

    public void loadTestData() {
        log.info("Загрузка тестовых данных...");

        try {
            loadHistoricalData("BTCUSDT", "1h", 200);
            loadHistoricalData("ETHUSDT", "30m", 150);

            log.info("Тестовые данные успешно загружены");

        } catch (Exception e) {
            log.error("Ошибка загрузки тестовых данных: {}", e.getMessage(), e);
        }
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void scheduledDataUpdate() {
        log.info("Запуск автоматического обновления данных...");
        loadTestData();
    }

    public void updateDataNow() {
        log.info("Ручное обновление данных...");
        loadTestData();
    }
}