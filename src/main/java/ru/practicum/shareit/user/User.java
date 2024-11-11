package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class User {
    private Integer id;
    @NotNull(message = "Имя не может быть пустым")
    private String name;
    @NotNull
    @Email(message = "Не корректная почта")
    private String email;
}
