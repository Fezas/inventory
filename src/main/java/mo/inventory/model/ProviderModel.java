/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.model;

import mo.inventory.entity.Provider;
import mo.inventory.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ProviderModel {
    public static void saveOrUpdateStructure(Provider provider) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(provider);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static List<Provider> getAll() {
        Transaction transaction = null;
        List<Provider> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.createQuery("SELECT a FROM Provider a", Provider.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static Provider getFromId(Long id) {
        Transaction transaction = null;
        Provider result = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.byId(Provider.class).load(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public ProviderModel() {
    }
}
