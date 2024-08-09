package ru.practicum.shareit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.user.UserClient;

@Configuration
public class WebClientConfig {
    @Value("${shareit-server.url}")
    private String shareitServerUrl;
    private static final String ITEMS_API_PREFIX = "/items";
    private static final String USERS_API_PREFIX = "/users";
    private static final String REQUESTS_API_PREFIX = "/requests";
    private static final String BOOKINGS_API_PREFIX = "/bookings";

    @Bean
    public ItemClient itemClient(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(shareitServerUrl + ITEMS_API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();

        return new ItemClient(restTemplate);
    }

    @Bean
    public UserClient userClientClient(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(shareitServerUrl + USERS_API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();

        return new UserClient(restTemplate);
    }

    @Bean
    public ItemRequestClient itemRequestClient(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(shareitServerUrl + REQUESTS_API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();

        return new ItemRequestClient(restTemplate);
    }

    @Bean
    public BookingClient bookingClient(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(shareitServerUrl + BOOKINGS_API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();

        return new BookingClient(restTemplate);
    }
}
