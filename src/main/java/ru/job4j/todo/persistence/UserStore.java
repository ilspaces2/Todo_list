package ru.job4j.todo.persistence;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@ThreadSafe
@Repository
public class UserStore {
    private final SessionFactory sessionFactory;

    public UserStore(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<User> add(User user) {
        Optional<User> userOptional = Optional.empty();
        try (Session session = sessionFactory.openSession();
        ) {
            session.beginTransaction();
            session.save(user);
            userOptional = Optional.of(user);
            session.getTransaction().commit();
        } catch (Exception ignored) {
        }
        return userOptional;
    }

    public Optional<User> findById(int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Optional user = session.createQuery("from User where id=:ID")
                .setParameter("ID", id)
                .uniqueResultOptional();
        session.getTransaction().commit();
        session.close();
        return user;
    }

    public boolean deleteAll() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        boolean rzl = session.createQuery("delete from User")
                .executeUpdate() > 0;
        session.getTransaction().commit();
        session.close();
        return rzl;
    }

    public Optional<User> findUserByNameAndPass(String username, String pass) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Optional user = session.createQuery("from User where userName=:nName and pass=:nPass")
                .setParameter("nPass", pass)
                .setParameter("nName", username)
                .uniqueResultOptional();
        session.getTransaction().commit();
        session.close();
        return user;
    }
}
