package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
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

    public Item add(Item item) {
        return store.add(item);
    }

    public boolean deleteById(int id) {
        return store.deleteById(id);
    }

    public boolean updateById(int id, Item item) {
        return store.updateById(id, item);
    }

    public Item findById(int id) {
        return store.findById(id);
    }

    public List<Item> findAllByIdUser(int idUser) {
        return store.findAllByIdUser(idUser);
    }

    public boolean deleteAll() {
        return store.deleteAll();
    }
}
