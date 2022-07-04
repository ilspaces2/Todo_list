package ru.job4j.todo.persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.Main;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class ItemStoreTest {

    private static final ItemStore STORE_ITEM = new ItemStore(new Main().sf());
    private static final UserStore STORE_USER = new UserStore(new Main().sf());
    private static final CategoryStore STORE_CATEGORY = new CategoryStore(new Main().sf());
    private static final User USER = new User(0, "Test", "Test");
    private static final Category CATEGORY = new Category("category");

    @Before
    public void addToTable() {
        STORE_CATEGORY.add(CATEGORY);
        STORE_USER.add(USER).get();
    }

    @After
    public void clearTable() {
        STORE_CATEGORY.deleteAll();
        STORE_ITEM.deleteAll();
        STORE_USER.deleteAll();
    }

    @Test
    public void whenAddItem() {
        Date time = new Date();
        Item item = new Item(0, "Desc", "Name", time, time, false, USER);
        item.addCategory(CATEGORY);
        STORE_ITEM.add(item);
        Item itemDB = STORE_ITEM.findById(item.getId());
        assertThat(itemDB.getItemName(), is(item.getItemName()));
        assertThat(itemDB.getCreated().getTime(), is(item.getCreated().getTime()));
        assertThat(itemDB.getUser().getId(), is(item.getUser().getId()));
    }

    @Test
    public void whenDeleteItem() {
        Item item = new Item(0, "Desc", "Name",
                new Date(), new Date(), false, USER);
        item.addCategory(CATEGORY);
        STORE_ITEM.add(item);
        assertTrue(STORE_ITEM.deleteById(item.getId()));
        Item itemDB = STORE_ITEM.findById(item.getId());
        assertNull(itemDB);
    }

    @Test
    public void whenUpdateItem() {
        Item item = new Item(0, "Desc", "Name",
                new Date(), new Date(), false, USER);
        item.addCategory(CATEGORY);
        STORE_ITEM.add(item);
        item.setDone(true);
        item.setDescription("New Desc");
        assertTrue(STORE_ITEM.updateById(item.getId(), item));
        assertTrue(item.isDone());
        assertThat(item.getDescription(), is("New Desc"));
    }

    @Test
    public void whenFindByIdUser() {
        User user = new User(0, "User", "User");
        STORE_USER.add(user).get();
        List<Item> list = List.of(
                new Item(0, "Desc", "Name",
                        new Date(), new Date(), false, USER),
                new Item(0, "Desc1", "Name1",
                        new Date(), new Date(), false, user),
                new Item(0, "Desc2", "Name2",
                        new Date(), new Date(), false, USER),
                new Item(0, "Desc3", "Name3",
                        new Date(), new Date(), false, user)
        );
        list.forEach(STORE_ITEM::add);
        assertThat(STORE_ITEM.findAllByIdUser(USER.getId()), is(List.of(list.get(0), list.get(2))));
    }
}
