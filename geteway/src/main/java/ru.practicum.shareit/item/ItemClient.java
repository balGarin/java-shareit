package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {


    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }


    public ResponseEntity<Object> getItemById(Integer ownerId, Integer itemId) {
        return get("/" + itemId, ownerId);
    }

    public ResponseEntity<Object> addItem(Integer ownerId, ItemDto item) {
        return post("", ownerId, item);
    }


    public ResponseEntity<Object> updateItem(Integer ownerId, Integer itemId, ItemDto item) {
        return patch("/" + itemId, item);
    }


    public ResponseEntity<Object> getAllItemsByOwner(Integer ownerId) {
        return get("", ownerId);
    }


    public ResponseEntity<Object> searchByQuery(Integer ownerId, String text) {
        Map<String, Object> parameters = Map.of(
                "search", text
        );
        return get("/search?search={search}", (long) ownerId, parameters);
    }


    public ResponseEntity<Object> addComment(Integer ownerId, Integer itemId, CommentDto comment) {
        return post("/" + itemId + "/comment", ownerId, comment);
    }

}
