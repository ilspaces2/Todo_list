package ru.job4j.todo.persistence;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;
import ru.job4j.Main;
import ru.job4j.todo.model.User;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserStoreTest {

    private static final UserStore STORE = new UserStore(new Main().sf());

    @After
    public void clearTable() {
        STORE.deleteAll();
    }

    @Test
    public void whenAddUser() {
        User user = new User(0, "Boris", "boris@yandex.ru");
        STORE.add(user);
        Optional<User> userDB = STORE.findById(user.getId());
        assertThat(user.getUserName(), is(userDB.get().getUserName()));
        assertThat(user.getPass(), is(userDB.get().getPass()));
    }

    @Test
    public void whenAddEqualsUsers() {
        User user1 = new User(0, "Max", "Max@yandex.ru");
        Optional<User> userOptional1 = STORE.add(user1);
        User user2 = new User(0, "Max", "Max@yandex.ru");
        Optional<User> userOptional2 = STORE.add(user2);
        assertFalse(userOptional1.isEmpty());
        assertTrue(userOptional2.isEmpty());
    }

    @Test
    public void whenFindByNameAndPass() {
        User user = new User(0, "Max", "Max@yandex.ru1");
        User user1 = new User(1, "Max1", "Max1@yandex.ru2");
        User user2 = new User(2, "Max2", "Max2@yandex.ru3");
        STORE.add(user);
        STORE.add(user1);
        STORE.add(user2);
        Optional<User> userOptional = STORE.findUserByNameAndPass(
                user.getUserName(), user.getPass());
        assertFalse(userOptional.isEmpty());
        assertThat(userOptional.get().getUserName(), is(user.getUserName()));
        assertThat(userOptional.get().getPass(), is(user.getPass()));
    }
}
