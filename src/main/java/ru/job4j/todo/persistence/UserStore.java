package ru.job4j.todo.persistence;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;
import java.util.function.Function;

@ThreadSafe
@Repository
public class UserStore {
    private final SessionFactory sessionFactory;

    public UserStore(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<User> add(User user) {
        try {
            return tx(session -> {
                session.save(user);
                return Optional.of(user);
            });
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<User> findById(int id) {
        return tx(session -> session.createQuery("from User where id=:ID")
                .setParameter("ID", id)
                .uniqueResultOptional());
    }

    public boolean deleteAll() {
        return tx(session -> session.createQuery("delete from User")
                .executeUpdate() > 0);
    }

    public Optional<User> findUserByNameAndPass(String username, String pass) {
        return tx(session -> session.createQuery("from User where userName=:nName and pass=:nPass")
                .setParameter("nPass", pass)
                .setParameter("nName", username)
                .uniqueResultOptional());
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
