package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.persistence.UserStore;

import java.util.Optional;

@ThreadSafe
@Service
public class UserService {

    private final UserStore store;

    public UserService(UserStore store) {
        this.store = store;
    }

    public Optional<User> add(User user) {
        return store.add(user);
    }

    public Optional<User> findById(int id) {
        return store.findById(id);
    }

    public boolean deleteAll() {
        return store.deleteAll();
    }

    public Optional<User> findUserByNameAndPass(String userName, String pass) {
        return store.findUserByNameAndPass(userName, pass);
    }
}
