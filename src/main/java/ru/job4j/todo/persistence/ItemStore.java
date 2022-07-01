package ru.job4j.todo.persistence;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@ThreadSafe
@Repository
public class ItemStore implements Store {

    private final SessionFactory sessionFactory;

    public ItemStore(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Item add(final Item item) {
        return tx(session -> {
            session.save(item);
            return item;
        }, sessionFactory);
    }

    public boolean deleteById(final int id) {
        return tx(session -> session.createQuery((" delete from Item where id=:ID"))
                .setParameter("ID", id)
                .executeUpdate() > 0, sessionFactory);
    }

    public boolean updateById(final int id, final Item item) {
        return tx(session -> session.createQuery(" update Item set "
                        + "itemName=:nName,description=:nDesc, created=:nCreated, finished=:nFinished,"
                        + "done=:nDone, user.id=:nUser where id=:ID")
                .setParameter("nName", item.getItemName())
                .setParameter("nDesc", item.getDescription())
                .setParameter("nCreated", item.getCreated())
                .setParameter("nFinished", item.getFinished())
                .setParameter("nDone", item.isDone())
                .setParameter("nUser", item.getUser().getId())
                .setParameter("ID", id)
                .executeUpdate() > 0, sessionFactory);
    }

    public boolean updateByIdWhenDone(final int id) {
        return tx(session -> session.createQuery(" update Item set finished=:nFinished,done=:nDone where id=:ID")
                .setParameter("nFinished", LocalDateTime.now())
                .setParameter("nDone", true)
                .setParameter("ID", id)
                .executeUpdate() > 0, sessionFactory);
    }

    public Item findById(final int id) {
        return (Item) tx(session -> session.createQuery(
                        "select distinct c from Item c join fetch c.categories where c.id=:ID")
                .setParameter("ID", id)
                .uniqueResult(), sessionFactory);
    }

    public List<Item> findAllByIdUser(final int idUser) {
        return tx(session -> session.createQuery("from Item where user.id=:nUserID ")
                .setParameter("nUserID", idUser)
                .list(), sessionFactory);
    }

    public boolean deleteAll() {
        return tx(session -> session.createQuery(" delete from Item ")
                .executeUpdate() > 0, sessionFactory);
    }
}
