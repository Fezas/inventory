/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.model;

import mo.inventory.entity.Active;
import mo.inventory.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ActiveModel {
    public static void saveOrUpdateStructure(Active active) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(active);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static Active getFromId(Long id) {
        Transaction transaction = null;
        Active result = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.byId(Active.class).load(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static List<Active> getAll() {
        Transaction transaction = null;
        List<Active> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.createQuery("SELECT a FROM Active a", Active.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public ActiveModel() {
    }
}
