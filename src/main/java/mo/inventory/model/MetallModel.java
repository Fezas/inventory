/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.model;

import mo.inventory.entity.Metall;
import mo.inventory.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class MetallModel {
    public static List<Metall> getAll() {
        Transaction transaction = null;
        List<Metall> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.createQuery("SELECT a FROM Metall a", Metall.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static Metall getFromId(String id) {
        Transaction transaction = null;
        Metall result = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.byId(Metall.class).load(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public MetallModel() {
    }
}
