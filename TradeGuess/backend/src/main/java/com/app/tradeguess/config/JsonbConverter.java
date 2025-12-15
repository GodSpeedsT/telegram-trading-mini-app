package com.app.tradeguess.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Converter(autoApply = true)
@Component
@RequiredArgsConstructor
public class JsonbConverter implements AttributeConverter<String,String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            if(attribute == null) return null;
            objectMapper.readTree(attribute);
            return attribute;
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid json",e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}
