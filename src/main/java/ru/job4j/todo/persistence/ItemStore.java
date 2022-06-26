package ru.job4j.todo.persistence;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.util.List;
import java.util.function.Function;

@ThreadSafe
@Repository
public class ItemStore {

    private final SessionFactory sessionFactory;

    public ItemStore(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Item add(final Item item) {
        return tx(session -> {
            session.save(item);
            return item;
        });
    }

    public boolean deleteById(final int id) {
        return tx(session -> session.createQuery((" delete from Item where id=:ID"))
                .setParameter("ID", id)
                .executeUpdate() > 0);
    }

    public boolean updateById(final int id, final Item item) {
        return tx(session -> session.createQuery(" update Item set "
                        + "itemName=:nName,description=:nDesc, created=:nCreated, finished=:nFinished,"
                        + "done=:nDone, userId=:nUserId where id=:ID")
                .setParameter("nName", item.getItemName())
                .setParameter("nDesc", item.getDescription())
                .setParameter("nCreated", item.getCreated())
                .setParameter("nFinished", item.getFinished())
                .setParameter("nDone", item.isDone())
                .setParameter("nUserId", item.getUserId())
                .setParameter("ID", id)
                .executeUpdate() > 0);
    }

    public Item findById(final int id) {
        return (Item) tx(session -> session.createQuery("from Item where id=:ID")
                .setParameter("ID", id)
                .uniqueResult());
    }

    public List<Item> findAllByIdUser(final int idUser) {
        return tx(session -> session.createQuery("from Item where userId=:nUserID ")
                .setParameter("nUserID", idUser)
                .list());
    }

    public boolean deleteAll() {
        return tx(session -> session.createQuery(" delete from Item ")
                .executeUpdate() > 0);
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sessionFactory.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
