package ee.ut.sauron;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceUtil {

    private static EntityManagerFactory entityManagerFactory;

    public static void initPersistence(){
        entityManagerFactory = Persistence.createEntityManagerFactory("SauronPersistenceUnit");
    }

    public static EntityManager getEntityManager(){
        return entityManagerFactory.createEntityManager();
    }
}
