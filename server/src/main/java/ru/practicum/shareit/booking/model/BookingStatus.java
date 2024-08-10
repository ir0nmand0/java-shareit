package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED
}