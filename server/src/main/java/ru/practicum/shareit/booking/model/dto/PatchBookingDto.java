package ru.practicum.shareit.booking.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record PatchBookingDto(
        LocalDateTime start,
        LocalDateTime end,
        BookingStatus status
) {
}
