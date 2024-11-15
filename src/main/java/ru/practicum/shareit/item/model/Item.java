package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.Objects;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer id;
    @NotNull(message = "Название не может быть пустым")
    @NotBlank
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Boolean available;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;
    @Transient
    private ItemRequest request;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        if (id == null || item.id == null) return false;
        return id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


