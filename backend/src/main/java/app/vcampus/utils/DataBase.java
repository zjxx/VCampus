package app.vcampus.utils;

import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import app.vcampus.domain.*;

import java.util.List;

/**
 * DataBase is a utility class for managing database operations using Hibernate.
 * It provides methods for initializing the database connection, performing CRUD operations,
 * and managing transactions.
 */
public class DataBase {
    private SessionFactory sessionFactory;
    private Session session;

    /**
     * Initializes the Hibernate session factory and opens a session.
     * Configures the session factory with annotated classes.
     */
    public void init() {
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(StoreItem.class)
                .addAnnotatedClass(StoreTransaction.class)
                .addAnnotatedClass(Student.class)
                .addAnnotatedClass(Book.class)
                .addAnnotatedClass(Reader2Book.class)
                .addAnnotatedClass(Course.class)
                .addAnnotatedClass(Enrollment.class)
                .addAnnotatedClass(Score.class)
                .addAnnotatedClass(ShoppingCartItem.class)
                .addAnnotatedClass(Video.class)
                .buildSessionFactory();
        session = sessionFactory.openSession();
    }

    /**
     * Constructs a DataBase instance and prints a connection message.
     */
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

    /**
     * Retrieves a list of all entities of the specified class.
     *
     * @param <T> the type of the entity
     * @param clazz the class of the entity
     * @return a list of all entities of the specified class
     */
    public <T> List<T> getAll(Class<T> clazz) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(clazz);
        criteria.from(clazz);
        return session.createQuery(criteria).getResultList();
    }

    /**
     * Retrieves a list of entities of the specified class where the given attribute matches the provided value using a LIKE query.
     *
     * @param <T> the type of the entity
     * @param clazz the class of the entity
     * @param attributeName the name of the attribute to filter by
     * @param value the value of the attribute to match
     * @return a list of entities that match the specified attribute and value using a LIKE query
     */
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

    /**
     * Deletes the given entity from the database.
     *
     * @param entity the entity to delete
     */
    public void delete(Object entity) {
        session.beginTransaction();
        session.delete(entity);
        session.getTransaction().commit();
    }

    /**
     * Updates the given entity in the database.
     *
     * @param entity the entity to update
     */
    public void update(Object entity) {
        session.beginTransaction();
        session.update(entity);
        session.getTransaction().commit();
    }

    /**
     * Saves the given entity to the database.
     * If the entity has a primary key, it will be updated; otherwise, it will be inserted.
     *
     * @param entity the entity to save
     */
    public void save(Object entity) {
        session.beginTransaction();
        session.save(entity);
        session.getTransaction().commit();
    }

    /**
     * Closes the Hibernate session and session factory.
     */
    public void close() {
        session.close();
        sessionFactory.close();
    }

    /**
     * Clears the Hibernate session.
     */
    public void clear() {
        session.clear();
    }

    /**
     * Disables foreign key checks in the database.
     */
    public void disableForeignKeyChecks() {
        session.beginTransaction();
        session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        session.getTransaction().commit();
    }

    /**
     * Enables foreign key checks in the database.
     */
    public void enableForeignKeyChecks() {
        session.beginTransaction();
        session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        session.getTransaction().commit();
    }

    /**
     * Evicts the given entity from the Hibernate session cache.
     *
     * @param entity the entity to evict
     */
    public void evict(Object entity) {
        session.evict(entity);
    }
}