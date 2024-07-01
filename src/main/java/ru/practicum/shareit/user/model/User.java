package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    private long id;
    @EqualsAndHashCode.Include
    private String email;
    private String name;

    public void setEmailWithVerification(@Email final String email) {
        this.email = email;
    }
}
