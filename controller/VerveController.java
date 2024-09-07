package com.task.verveservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class VerveController {

    private static final Logger logger = LoggerFactory.getLogger(VerveController.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // Accept endpoint
    @GetMapping("/api/verve/accept")
    public String accept(@RequestParam int id, @RequestParam(required = false) String endpoint) {
        try {
            String key = "request:" + id;
            Boolean isUnique = redisTemplate.opsForValue().setIfAbsent(key, "1", 1, TimeUnit.MINUTES);

            if (Boolean.TRUE.equals(isUnique)) {
                redisTemplate.opsForValue().increment("uniqueCount");
            }

            if (endpoint != null && !endpoint.isEmpty()) {
                sendPostRequestToEndpoint(endpoint);
            }

            return "ok";
        } catch (Exception e) {
            logger.error("Error processing request: ", e);
            return "failed";
        }
    }

    // Scheduled task for sending the count to Kafka
    @Scheduled(fixedRate = 60000)
    public void sendUniqueRequestsToKafka() {
        try {
            String count = redisTemplate.opsForValue().getAndSet("uniqueCount", "0");
            kafkaTemplate.send("unique-requests-topic", count);  // Send message to Kafka topic
            logger.info("Sent unique requests count to Kafka: {}", count);
        } catch (Exception e) {
            // Handle Kafka connection failure
            logger.error("Failed to send unique requests to Kafka", e);
        }
    }

    // Send POST requesest
    private void sendPostRequestToEndpoint(String endpoint) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("count", redisTemplate.opsForValue().get("uniqueCount"));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            HttpStatus statusCode = restTemplate.postForEntity(endpoint, request, String.class).getStatusCode();

            logger.info("HTTP POST request to {} returned status code: {}", endpoint, statusCode);
        } catch (Exception e) {
            logger.error("Error sending POST request to endpoint: ", e);
        }
    }
}
