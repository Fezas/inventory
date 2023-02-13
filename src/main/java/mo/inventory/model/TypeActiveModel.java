/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.model;

import mo.inventory.entity.TypeActive;
import mo.inventory.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TypeActiveModel {
    public static List<TypeActive> getAll() {
        Transaction transaction = null;
        List<TypeActive> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.createQuery("SELECT a FROM TypeActive a", TypeActive.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }
    public static TypeActive getFromId(String id) {
        Transaction transaction = null;
        TypeActive result = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.byId(TypeActive.class).load(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }
    public TypeActiveModel() {
    }
}
