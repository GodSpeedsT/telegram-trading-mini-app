//package com.app.tradeguess.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.net.URLDecoder;
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.util.*;
//
//@Slf4j
//@Service
//public class TelegramAuthService {
//
//    @Value("${telegram.bot.token}")
//    private String botToken;
//
//    /**
//     * Упрощенная проверка данных Telegram
//     * Для MVP просто проверяем наличие основных полей
//     */
//    public boolean validateTelegramData(String initData) {
//        if (initData == null || initData.isEmpty()) {
//            return false;
//        }
//
//        try {
//            Map<String, String> params = parseInitData(initData);
//
//            // Проверяем наличие обязательных полей
//            if (!params.containsKey("hash") || !params.containsKey("user")) {
//                return false;
//            }
//
//            // Для MVP можно пропустить проверку подписи
//            // В продакшене нужно раскомментировать:
//            // return validateHash(params);
//
//            return true;
//
//        } catch (Exception e) {
//            log.error("Error validating Telegram data: {}", e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * Извлечение данных пользователя из initData
//     */
//    public TelegramUser extractUser(String initData) {
//        try {
//            Map<String, String> params = parseInitData(initData);
//            String userJson = params.get("user");
//
//            if (userJson == null) {
//                throw new IllegalArgumentException("User data not found in initData");
//            }
//
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode userNode = mapper.readTree(userJson);
//
//            TelegramUser user = new TelegramUser();
//            user.setId(userNode.get("id").asLong());
//            user.setUsername(userNode.has("username") ? userNode.get("username").asText() : null);
//            user.setFirstName(userNode.has("first_name") ? userNode.get("first_name").asText() : null);
//            user.setLastName(userNode.has("last_name") ? userNode.get("last_name").asText() : null);
//
//            return user;
//
//        } catch (Exception e) {
//            throw new IllegalArgumentException("Invalid user data in initData", e);
//        }
//    }
//
//    /**
//     * Парсинг строки initData в Map
//     */
//    private Map<String, String> parseInitData(String initData) {
//        Map<String, String> params = new HashMap<>();
//
//        String[] pairs = initData.split("&");
//        for (String pair : pairs) {
//            int idx = pair.indexOf("=");
//            if (idx > 0) {
//                String key = pair.substring(0, idx);
//                String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
//                params.put(key, value);
//            }
//        }
//
//        return params;
//    }
//
//    /**
//     * Валидация подписи Telegram (для продакшена)
//     */
//    @SuppressWarnings("unused")
//    private boolean validateHash(Map<String, String> params) {
//        try {
//            String hash = params.get("hash");
//            params.remove("hash");
//
//            List<String> keys = new ArrayList<>(params.keySet());
//            Collections.sort(keys);
//
//            StringBuilder dataCheckString = new StringBuilder();
//            for (String key : keys) {
//                if (!key.equals("hash")) {
//                    dataCheckString.append(key).append("=").append(params.get(key)).append("\n");
//                }
//            }
//
//            if (dataCheckString.length() > 0) {
//                dataCheckString.deleteCharAt(dataCheckString.length() - 1);
//            }
//
//            String secretKey = MessageDigest.getInstance("SHA-256")
//                    .digest(botToken.getBytes(StandardCharsets.UTF_8));
//
//            String computedHash = bytesToHex(MessageDigest.getInstance("SHA-256")
//                    .digest((dataCheckString.toString() + secretKey).getBytes(StandardCharsets.UTF_8)));
//
//            return hash.equals(computedHash);
//
//        } catch (Exception e) {
//            log.error("Error computing hash: {}", e.getMessage());
//            return false;
//        }
//    }
//
//    private String bytesToHex(byte[] bytes) {
//        StringBuilder hexString = new StringBuilder();
//        for (byte b : bytes) {
//            String hex = Integer.toHexString(0xff & b);
//            if (hex.length() == 1) {
//                hexString.append('0');
//            }
//            hexString.append(hex);
//        }
//        return hexString.toString();
//    }
//
//    @Data
//    public static class TelegramUser {
//        private Long id;
//        private String username;
//        private String firstName;
//        private String lastName;
//    }
//}