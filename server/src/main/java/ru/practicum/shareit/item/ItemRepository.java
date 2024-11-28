package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query(" select i from Item i " +
            "where ((i.name) ilike (concat('%', ?1, '%')) " +
            "or (i.description) ilike (concat('%', ?1, '%')))" +
            "and i.available=true")
    List<Item> findItemsByQuery(String text);

    List<Item> findAllByOwnerId(Integer ownerId);

    List<Item> findAllByRequestIdIn(List<Integer> ids);

    List<Item> findAllByRequestId(Integer requestId);


}
