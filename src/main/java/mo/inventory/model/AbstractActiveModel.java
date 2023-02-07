/*
 * Copyright (c) 2022-2023. Stepantsov P.V.
 */

package mo.inventory.model;

import mo.inventory.entity.AbstractActive;
import mo.inventory.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Класс формирования структур и объектов типа {@link AbstractActive}
 * @autor Stepantsov P.V.
 */

public class AbstractActiveModel {

    public static AbstractActive getFromId(Long id) {
        Transaction transaction = null;
        AbstractActive result = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.byId(AbstractActive.class).load(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static void saveOrUpdateCategory(AbstractActive active) {
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


    public static void delete(AbstractActive active) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(active);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public AbstractActiveModel() {
    }

}
