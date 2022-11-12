package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.persistence.ItemStore;

import java.util.List;

@ThreadSafe
@Service
public class ItemService {

    private final ItemStore store;

    public ItemService(ItemStore store) {
        this.store = store;
    }

    @CacheEvict(value = "items", allEntries = true)
    public Item add(Item item) {
        return store.add(item);
    }

    @CacheEvict(value = "items", allEntries = true)
    public boolean deleteById(int id) {
        return store.deleteById(id);
    }

    @CacheEvict(value = "items", allEntries = true)
    public boolean updateById(int id, Item item) {
        return store.updateById(id, item);
    }

    public Item findById(int id) {
        return store.findById(id);
    }

    @Cacheable("items")
    public List<Item> findAllByIdUser(int idUser) {
        return store.findAllByIdUser(idUser);
    }

    public boolean deleteAll() {
        return store.deleteAll();
    }

    public boolean updateByIdWhenDone(int id) {
        return store.updateByIdWhenDone(id);
    }
}
