package com.samtheo.instructif.dao;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

/**
 * Cette classe fournit des méthodes statiques utiles pour accéder aux
 * fonctionnalités de JPA (Entity Manager, Entity Transaction). Le nom de
 * l'unité de persistance (PERSISTENCE_UNIT_NAME) doit être conforme à la
 * configuration indiquée dans le fichier persistence.xml du projet.
 *
 * @author DASI Team
 */
public class JpaUtil {

    public static final String PERSISTENCE_UNIT_NAME = "instructif-PU";

    private static EntityManagerFactory entityManagerFactory = null;

    private static final ThreadLocal<EntityManager> threadLocalEntityManager = new ThreadLocal<EntityManager>() {
        @Override
        protected EntityManager initialValue() {
            return null;
        }
    };

    private static boolean JPAUTIL_LOG_ACTIVE = true;

    private static void log(String message) {
        if (JPAUTIL_LOG_ACTIVE) {
            System.out.println("[JpaUtil:Log] " + message);
        }
    }

    public static void desactiverLog() {
        JPAUTIL_LOG_ACTIVE = false;
    }

    public static synchronized void creerFabriquePersistance() {
        creerFabriquePersistance(null);
    }

    // schemaGenerationAction : "drop-and-create" pour réinitialiser, "none"
    // pour préserver la base, null pour garder la valeur du persistence.xml.
    public static synchronized void creerFabriquePersistance(String schemaGenerationAction) {
        log("Création de la fabrique de contexte de persistance");
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
        Map<String, String> propertyMap = new HashMap<>();
        if (!JPAUTIL_LOG_ACTIVE) {
            propertyMap.put("eclipselink.logging.level", "OFF");
        }
        if (schemaGenerationAction != null) {
            propertyMap.put("javax.persistence.schema-generation.database.action",
                    schemaGenerationAction);
        }
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, propertyMap);
    }

    public static synchronized void fermerFabriquePersistance() {
        log("Fermeture de la fabrique de contexte de persistance");
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
            entityManagerFactory = null;
        }
    }

    public static void creerContextePersistance() {
        log("Création du contexte de persistance");
        threadLocalEntityManager.set(entityManagerFactory.createEntityManager());
    }

    public static void fermerContextePersistance() {
        log("Fermeture du contexte de persistance");
        EntityManager em = threadLocalEntityManager.get();
        em.close();
        threadLocalEntityManager.set(null);
    }

    public static void ouvrirTransaction() throws Exception {
        log("Ouverture de la transaction (begin)");
        try {
            EntityManager em = threadLocalEntityManager.get();
            em.getTransaction().begin();
        } catch (Exception ex) {
            log("Erreur lors de l'ouverture de la transaction");
            throw ex;
        }
    }

    public static void validerTransaction() throws RollbackException, Exception {
        log("Validation de la transaction (commit)");
        try {
            EntityManager em = threadLocalEntityManager.get();
            em.getTransaction().commit();
        } catch (Exception ex) {
            log("Erreur lors de la validation (commit) de la transaction");
            throw ex;
        }
    }

    public static void annulerTransaction() {
        try {
            log("Annulation de la transaction (rollback)");
            EntityManager em = threadLocalEntityManager.get();
            if (em.getTransaction().isActive()) {
                log("Annulation effective de la transaction (rollback d'une transaction active)");
                em.getTransaction().rollback();
            }
        } catch (Exception ex) {
            log("Erreur lors de l'annulation (rollback) de la transaction");
        }
    }

    protected static EntityManager obtenirContextePersistance() {
        log("Obtention du contexte de persistance");
        return threadLocalEntityManager.get();
    }
}
