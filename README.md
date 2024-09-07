# Sprinboot-app-process-10K-requests-per-second-using-redis-and-kafka
REST service which is able to process at least 10K requests per second.
# Verve Service: Design and Implementation

## Overview
We built a high-performance REST API using Spring Boot to handle 10,000+ requests per second, with features for request deduplication, logging, and external service integration.

## Key Design Choices
1. Used Spring Boot for performance and ease of development
2. Implemented Redis for distributed caching and request deduplication
3. Integrated Kafka for streaming unique request counts
4. Designed for scalability behind a load balancer

## Implementation Steps
1. Created a REST controller with GET endpoint `/api/verve/accept`
2. Used Redis for request deduplication across multiple instances
3. Implemented logging for unique request counts and HTTP responses
4. Added scheduled task to process and send data to Kafka every minute
5. Used Spring's RestTemplate for external HTTP calls

## Challenges and Solutions
- Handled high concurrency using thread-safe operations
- Ensured consistent state across instances with Redis
- Optimized for performance using caching and efficient queries

## Potential Improvements
1. Implement rate limiting
2. Add comprehensive testing
3. Implement circuit breakers for external calls
4. Consider using reactive programming for higher throughput

This implementation aims to meet all specified requirements while ensuring high performance, scalability, and reliability.
