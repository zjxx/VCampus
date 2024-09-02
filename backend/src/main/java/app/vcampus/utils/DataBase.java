package app.vcampus.utils;

import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import app.vcampus.domain.*;

import java.util.List;

public class DataBase {
    private SessionFactory sessionFactory;
    private Session session;

    public void init() {
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(StoreItem.class)
                .addAnnotatedClass(StoreTransaction.class)
                .addAnnotatedClass(Student.class)
                .addAnnotatedClass(Book.class)
                .addAnnotatedClass(Reader2Book.class)
                .buildSessionFactory();
        session = sessionFactory.openSession();
    }

    public DataBase() {
        System.out.println("Database connected.");
    }


    /**
     * Retrieves a list of entities of the specified class where the given attribute matches the provided value.
     *
     * @param <T> the type of the entity
     * @param clazz the class of the entity
     * @param attributeName the name of the attribute to filter by
     * @param value the value of the attribute to match
     * @return a list of entities that match the specified attribute and value
     */
    public <T> List<T> getWhere(Class<T> clazz, String attributeName, Object value) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(clazz);
        Root<T> itemRoot = criteria.from(clazz);
        criteria.where(builder.equal(itemRoot.get(attributeName), value));
        return session.createQuery(criteria).getResultList();
    }

    public <T> List<T> getAll(Class<T> clazz) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(clazz);
        criteria.from(clazz);
        return session.createQuery(criteria).getResultList();
    }

    //模糊搜索
    public <T> List<T> getLike(Class<T> clazz, String attributeName, String value) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(clazz);
        Root<T> itemRoot = criteria.from(clazz);
        criteria.where(builder.like(itemRoot.get(attributeName), "%" + value + "%"));
        return session.createQuery(criteria).getResultList();
    }
    /**
     * Persists the given entity to the database.
     *
     * @param entity the entity to persist
     */
    public void persist(Object entity) {
        session.beginTransaction();
        session.persist(entity);
        session.getTransaction().commit();
    }

    /**
     * Removes the given entity from the database.
     *
     * @param entity the entity to remove
     */
    public void remove(Object entity) {
        session.beginTransaction();
        session.remove(entity);
        session.getTransaction().commit();
    }


    public void delete(Object entity) {
        session.beginTransaction();
        session.delete(entity);
        session.getTransaction().commit();
    }

    public void update(Object entity) {
        session.beginTransaction();
        session.update(entity);
        session.getTransaction().commit();
    }

    public void save(Object entity) {
        session.beginTransaction();
        session.save(entity);
        session.getTransaction().commit();
    }


    public void close() {
        session.close();
        sessionFactory.close();
    }
}