/*
 * Copyright (c) 2022-2023. Stepantsov P.V.
 */

package mo.inventory.model;

import mo.inventory.entity.CategoryActive;
import mo.inventory.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс формирования структур и объектов типа {@link CategoryActive}
 * @autor Stepantsov P.V.
 */

public class CategoryActiveModel {

    public static CategoryActive getFromId(Long id) {
        Transaction transaction = null;
        CategoryActive result = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.byId(CategoryActive.class).load(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static void saveOrUpdateCategory(CategoryActive category) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(category);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static CategoryActive getRootCategory() {
        Transaction transaction = null;
        CategoryActive result = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.createQuery("SELECT a FROM CategoryActive a WHERE a.isRoot = true", CategoryActive.class).getSingleResult();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static List<CategoryActive> getFromIdCategoryActive(long idNode) {
        Transaction transaction = null;
        List<CategoryActive> result = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.createQuery("SELECT a FROM CategoryActive a WHERE a.idCategory = '" + idNode + "' AND a.isRoot = false", CategoryActive.class).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public static void delete(CategoryActive category) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(category);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public CategoryActiveModel() {
    }

}
