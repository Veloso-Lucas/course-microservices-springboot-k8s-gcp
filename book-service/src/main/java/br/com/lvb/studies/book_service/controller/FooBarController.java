package br.com.lvb.studies.book_service.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hands-on examples for Resilience4j patterns.
 * <p>
 * Try each endpoint with curl or a browser and watch the logs / responses:
 * <ul>
 *   <li>GET /book-service/resilience/retry</li>
 *   <li>GET /book-service/resilience/circuit-breaker</li>
 *   <li>GET /book-service/resilience/rate-limit</li>
 *   <li>GET /book-service/resilience/bulkhead</li>
 * </ul>
 */
@Tag(name = "Foo Bar Endpoint")
@RestController
@RequestMapping("book-service/resilience")
public class FooBarController {

    private static final Logger logger = LoggerFactory.getLogger(FooBarController.class);

    // Counters used only to simulate failures / slowness in these demos (not for production use).
    private int retryInvocationCount;
    private int bulkheadActiveCalls;

    /**
     * Retry: automatically re-invokes the method when a transient failure occurs.
     * <p>
     * This demo throws on the first 3 invocations; Resilience4j retries until success
     * or until {@code max-attempts} is reached (see {@code resilience4j.retry.instances.foo-bar-retry}).
     * <p>
     * Example: curl http://localhost:8100/book-service/resilience/retry
     */
    @GetMapping("/retry")
    @Retry(name = "foo-bar-retry", fallbackMethod = "retryFallback")
    public synchronized String retryExample() {
        retryInvocationCount++;
        logger.info("Retry example - invocation #{}", retryInvocationCount);

        if (retryInvocationCount <= 3) {
            throw new RuntimeException("Simulated transient failure on invocation " + retryInvocationCount);
        }

        retryInvocationCount = 0;
        return "Succeeded after automatic retries";
    }

    /**
     * Circuit Breaker: stops calling a failing dependency and returns a fallback instead.
     * <p>
     * This demo always throws. After enough failures the circuit opens and the fallback
     * is returned immediately without executing this method (see {@code resilience4j.circuitbreaker.instances.foo-bar-circuit-breaker}).
     * <p>
     * Example: call several times in a row and observe the circuit opening in the logs.
     */
    @GetMapping("/circuit-breaker")
    @CircuitBreaker(name = "foo-bar-circuit-breaker", fallbackMethod = "circuitBreakerFallback")
    public String circuitBreakerExample() {
        logger.info("Circuit breaker example - method executed (downstream is simulated as down)");
        throw new RuntimeException("Simulated downstream failure");
    }

    /**
     * Rate Limiter: caps how many calls are allowed per time window.
     * <p>
     * Configured for 2 calls every 10 seconds. Extra calls are rejected and the fallback runs
     * (see {@code resilience4j.ratelimiter.instances.foo-bar-rate-limit}).
     * <p>
     * Example: curl the URL 3+ times within 10 seconds.
     */
    @GetMapping("/rate-limit")
    @RateLimiter(name = "foo-bar-rate-limit", fallbackMethod = "rateLimiterFallback")
    public String rateLimiterExample() {
        logger.info("Rate limiter example - call permitted");
        return "Call permitted within the rate limit";
    }

    /**
     * Bulkhead: limits how many concurrent executions can run at the same time.
     * <p>
     * This demo sleeps for 5 seconds to simulate a slow call. With {@code max-concurrent-calls: 2},
     * a third simultaneous request is rejected and the fallback runs
     * (see {@code resilience4j.bulkhead.instances.foo-bar-bulkhead}).
     * <p>
     * Example: open 3 tabs or run 3 parallel curls and watch the third one fail fast.
     */
    @GetMapping("/bulkhead")
    @Bulkhead(name = "foo-bar-bulkhead", fallbackMethod = "bulkheadFallback")
    public String bulkheadExample() throws InterruptedException {
        bulkheadActiveCalls++;
        logger.info("Bulkhead example - started (active calls: {})", bulkheadActiveCalls);

        try {
            Thread.sleep(5_000);
            return "Slow call completed";
        } finally {
            bulkheadActiveCalls--;
            logger.info("Bulkhead example - finished (active calls: {})", bulkheadActiveCalls);
        }
    }

    // Fallback methods must keep the same return type and parameters as the guarded method, plus an Exception.

    public String retryFallback(Exception exception) {
        logger.warn("Retry fallback triggered: {}", exception.getMessage());
        return "Retry fallback: all attempts exhausted";
    }

    public String circuitBreakerFallback(Exception exception) {
        logger.warn("Circuit breaker fallback triggered: {}", exception.getMessage());
        return "Circuit breaker fallback: circuit is open or call failed";
    }

    public String rateLimiterFallback(Exception exception) {
        logger.warn("Rate limiter fallback triggered: {}", exception.getMessage());
        return "Rate limiter fallback: too many requests in the current window";
    }

    public String bulkheadFallback(Exception exception) {
        logger.warn("Bulkhead fallback triggered: {}", exception.getMessage());
        return "Bulkhead fallback: max concurrent calls reached";
    }
}
