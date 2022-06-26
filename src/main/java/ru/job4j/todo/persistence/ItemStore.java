package ru.job4j.todo.persistence;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.util.List;

@ThreadSafe
@Repository
public class ItemStore {

    private final SessionFactory sessionFactory;

    public ItemStore(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Item add(Item item) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    public boolean deleteById(int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        boolean rzl = session.createQuery(" delete from Item where id=:ID")
                .setParameter("ID", id)
                .executeUpdate() > 0;
        session.getTransaction().commit();
        session.close();
        return rzl;
    }

    public boolean updateById(int id, Item item) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        boolean rzl = session.createQuery(" update Item set "
                        + "itemName=:nName,description=:nDesc, created=:nCreated, finished=:nFinished,"
                        + "done=:nDone, userId=:nUserId where id=:ID")
                .setParameter("nName", item.getItemName())
                .setParameter("nDesc", item.getDescription())
                .setParameter("nCreated", item.getCreated())
                .setParameter("nFinished", item.getFinished())
                .setParameter("nDone", item.isDone())
                .setParameter("nUserId", item.getUserId())
                .setParameter("ID", id)
                .executeUpdate() > 0;
        session.getTransaction().commit();
        session.close();
        return rzl;
    }

    public Item findById(int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Object item = session.createQuery("from Item where id=:ID")
                .setParameter("ID", id)
                .uniqueResult();
        session.getTransaction().commit();
        session.close();
        return (Item) item;
    }

    public List<Item> findAllByIdUser(int idUser) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List list = session.createQuery("from Item where userId=:nUserID ")
                .setParameter("nUserID", idUser)
                .list();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    public boolean deleteAll() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        boolean rzl = session.createQuery(" delete from Item ")
                .executeUpdate() > 0;
        session.getTransaction().commit();
        session.close();
        return rzl;
    }
}
