package app.vcampus.utils;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import app.vcampus.domain.*;

import java.util.List;

public class DataBase {
    private SessionFactory sessionFactory ;
    private  Session session ;
    public void init(){
        Configuration configuration = new Configuration().configure();
        sessionFactory = configuration
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(StoreItem.class)
                .buildSessionFactory();
        session = sessionFactory.openSession();

    }

    public DataBase() {
        System.out.println("Database connected.");
    }


    public List<User> getWhereName(String name) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> itemRoot = criteria.from(User.class);
        criteria.where(builder.equal(itemRoot.get("username"), name));
        return session.createQuery(criteria).getResultList();
    }

    public void close(){
        session.close();
        sessionFactory.close();
    }
}
