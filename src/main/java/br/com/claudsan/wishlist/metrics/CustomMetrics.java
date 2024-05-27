package br.com.claudsan.wishlist.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomMetrics {

    public static final String WISHLIST_CREATED = "wishlist_created";
    public static final String WISHLIST_ADD_PRODUCTS = "wishlist_add_products";

    private final MeterRegistry registry;

    public void counter(String name) {
        Counter.builder(name)
                .register(registry)
                .increment();
    }

}