package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.persistence.CategoryStore;

import java.util.List;

@ThreadSafe
@Service
public class CategoryService {

    private CategoryStore store;

    public CategoryService(CategoryStore store) {
        this.store = store;
    }

    public List<Category> findAll() {
        return store.findAll();
    }

    public boolean deleteAll() {
        return store.deleteAll();
    }

    public Category findById(int id) {
        return store.findById(id);
    }
}
