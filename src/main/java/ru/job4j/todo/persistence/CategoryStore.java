package ru.job4j.todo.persistence;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.List;

@ThreadSafe
@Repository
public class CategoryStore implements Store {

    private SessionFactory sessionFactory;

    public CategoryStore(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Category add(final Category category) {
        return tx(session -> {
            session.save(category);
            return category;
        }, sessionFactory);
    }

    public List<Category> findAll() {
        return tx(session ->
                session.createQuery("from Category ").list(), sessionFactory);
    }

    public boolean deleteAll() {
        return tx(session ->
                session.createQuery(" delete from Category ")
                        .executeUpdate() > 0, sessionFactory);
    }

    public Category findById(final int id) {
        return (Category) tx(session ->
                session.createQuery("from Category where id=:ID")
                        .setParameter("ID", id)
                        .uniqueResult(), sessionFactory);
    }
}
