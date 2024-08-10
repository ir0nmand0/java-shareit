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
    @Value("${shareit-server.items}")
    private String itemsApiPrefix;
    @Value("${shareit-server.users}")
    private String usersApiPrefix;
    @Value("${shareit-server.requests}")
    private String requestsApiPrefix;
    @Value("${shareit-server.bookings}")
    private String bookingsApiPrefix;
    private static final HttpComponentsClientHttpRequestFactory FACTORY = new HttpComponentsClientHttpRequestFactory();

    @Bean
    public ItemClient itemClient(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(shareitServerUrl + itemsApiPrefix))
                .requestFactory(() -> FACTORY)
                .build();

        return new ItemClient(restTemplate);
    }

    @Bean
    public UserClient userClientClient(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(shareitServerUrl + usersApiPrefix))
                .requestFactory(() -> FACTORY)
                .build();

        return new UserClient(restTemplate);
    }

    @Bean
    public ItemRequestClient itemRequestClient(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(shareitServerUrl + requestsApiPrefix))
                .requestFactory(() -> FACTORY)
                .build();

        return new ItemRequestClient(restTemplate);
    }

    @Bean
    public BookingClient bookingClient(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(shareitServerUrl + bookingsApiPrefix))
                .requestFactory(() -> FACTORY)
                .build();

        return new BookingClient(restTemplate);
    }
}
