/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.model;

import mo.inventory.entity.SettingMainTable;
import mo.inventory.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.Map;

public class SettingMainTableModel {
    public static void saveOrUpdateStructure(SettingMainTable setting) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(setting);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static SettingMainTable getFromId(Long id) {
        Transaction transaction = null;
        SettingMainTable result = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            result = session.byId(SettingMainTable.class).load(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return result;
    }

    public Map<String, Boolean> getMapVisible(Long id) {
        Map<String, Boolean> mapVisible = new HashMap<String, Boolean>();
        SettingMainTable setting = getFromId(id);
        mapVisible.put("abstractActive", setting.isClmnTitle());
        mapVisible.put("persona", setting.isClmnMol());
        mapVisible.put("newPersona", setting.isClmnNewMol());
        mapVisible.put("amount", setting.isClmnAmount());
        mapVisible.put("accountNumber", setting.isClmnAccountNumber());
        mapVisible.put("factoryNumber", setting.isClmnFactoryNumber());
        mapVisible.put("inventoryNumber", setting.isClmnInventoryNumber());
        mapVisible.put("dateComissioning", setting.isClmnDateComissions());
        mapVisible.put("dateAccounting", setting.isClmnDateAccounting());
        mapVisible.put("functionActive", setting.isClmnFunctionActive());
        mapVisible.put("statusActive", setting.isClmnStatusActive());
        mapVisible.put("provider", setting.isClmnProvider());
        return mapVisible;
    }

    public SettingMainTableModel() {
    }
}
