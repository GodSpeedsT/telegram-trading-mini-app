//package com.app.tradeguess.service.data;
//
//import com.app.tradeguess.model.entity.ChartSegment;
//import com.app.tradeguess.repository.ChartSegmentRepository;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class BinanceDataService {
//
//    private final ChartSegmentRepository chartSegmentRepository;
//    private final RestTemplate restTemplate;
//    private final ObjectMapper objectMapper;
//
//    // Пример метода для разовой загрузки исторических данных
//    public void loadHistoricalData(String symbol, String interval, int limit) {
//        String url = String.format(
//                "https://api.binance.com/api/v3/klines?symbol=%s&interval=%s&limit=%d",
//                symbol, interval, limit
//        );
//
//        String response = restTemplate.getForObject(url, String.class);
//        List<ChartSegment> segments = parseKlineData(response);
//        chartSegmentRepository.saveAll(segments);
//    }
//
//    private List<ChartSegment> parseKlineData(String jsonResponse) {
//        List<ChartSegment> segments = new ArrayList<>();
//        try {
//            JsonNode rootNode = objectMapper.readTree(jsonResponse);
//            if (rootNode.isArray()) {
//                for (JsonNode kline : rootNode) {
//                    ChartSegment segment = new ChartSegment();
//
//                    // Предположим, берем первые N свечей для отображения
//                    String displayCandles = buildCandlesJson(kline, 5); // Первые 5 свечей
//                    String resultCandles = buildCandlesJson(kline, 10); // Следующие 10 свечей
//
//                    segment.setDisplayCandles(displayCandles);
//                    segment.setResultCandles(resultCandles);
//
//                    // Расчет направления: 1 = рост, -1 = падение
//                    BigDecimal open = new BigDecimal(kline.get(1).asText());
//                    BigDecimal close = new BigDecimal(kline.get(4).asText());
//                    segment.setCalculatedDirection(close.compareTo(open) > 0 ? 1 : -1);
//
//                    // Цены для расчета
//                    segment.setPriceAtDecision(open);
//                    segment.setPriceAtTarget(close);
//
//                    segments.add(segment);
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Ошибка парсинга данных Binance", e);
//        }
//        return segments;
//    }
//
//    // Вспомогательный метод для формирования JSON свечей
//    private String buildCandlesJson(JsonNode baseKline, int count) {
//        // Логика генерации массива свечей на основе базовой
//        // Можно использовать случайные данные или реальные исторические
//        return "[...]"; // Возвращаем JSON строку
//    }
//
//    // Запускать каждые 24 часа для обновления данных
//    @Scheduled(cron = "0 0 3 * * ?") // Каждый день в 3:00
//    public void scheduledDataUpdate() {
//        loadHistoricalData("BTCUSDT", "5m", 500);
//    }
//}