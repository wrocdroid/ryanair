package com.home.dev.ifs.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

@Configuration
public class JacksonConfiguration {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = ofPattern("yyyy-MM-dd'T'HH:mm");
    public static final DateTimeFormatter TIME_FORMATTER = ofPattern("HH:mm");

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        mapper.registerModule(javaTimeModule);
        return mapper;
    }

    public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return LocalDateTime.parse(p.getValueAsString(), DATE_TIME_FORMATTER);
        }
    }

    public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(DATE_TIME_FORMATTER));
        }
    }
}
