package com.samtheo.instructif.dao;

import com.samtheo.instructif.metier.modele.Intervenant;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class IntervenantDAO {

    public void create(Intervenant intervenant) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(intervenant);
    }

    public Intervenant findById(Long id) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.find(Intervenant.class, id);
    }

    public Intervenant findByLogin(String login) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Intervenant> q = em.createQuery(
                "SELECT i FROM Intervenant i WHERE i.login = :login",
                Intervenant.class);
        q.setParameter("login", login);
        try {
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Intervenant authentifier(String login, String motDePasse) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Intervenant> q = em.createQuery(
                "SELECT i FROM Intervenant i WHERE i.login = :login AND i.motDePasse = :mdp",
                Intervenant.class);
        q.setParameter("login", login);
        q.setParameter("mdp", motDePasse);
        try {
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public List<Intervenant> findAll() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.createQuery(
                "SELECT i FROM Intervenant i ORDER BY i.nom ASC, i.prenom ASC",
                Intervenant.class).getResultList();
    }

    /**
     * Intervenants dont la plage de niveaux scolaires englobe le niveau cible
     * et qui n'ont aucune demande EN_COURS actuellement affectée. Triés par
     * nombre d'interventions croissant (équilibrage de charge).
     * <p>
     * <b>Convention scolaire FR :</b> niveauMin = niveau scolaire le plus
     * jeune accepté (ex. 6e = 6), niveauMax = niveau le plus avancé (ex. 3e
     * = 3). Numériquement, niveauMin &ge; niveauMax. La plage est donc :
     * niveauMax &le; niveau &le; niveauMin.
     */
    public List<Intervenant> findDisponiblesPourNiveau(int niveau) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Intervenant> q = em.createQuery(
                "SELECT i FROM Intervenant i " +
                "WHERE i.niveauMax <= :n AND i.niveauMin >= :n " +
                "  AND i NOT IN (" +
                "      SELECT d.intervenant FROM Demande d " +
                "       WHERE d.intervenant IS NOT NULL " +
                "         AND d.statut = com.samtheo.instructif.metier.modele.Statut.EN_COURS" +
                "  ) " +
                "ORDER BY SIZE(i.demandes) ASC, i.nom ASC",
                Intervenant.class);
        q.setParameter("n", niveau);
        return q.getResultList();
    }
}
