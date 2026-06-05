package com.samtheo.instructif.dao;

import com.samtheo.instructif.metier.modele.Bilan;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class BilanDAO {

    public void create(Bilan bilan) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(bilan);
    }

    public Bilan findById(Long id) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.find(Bilan.class, id);
    }

    public Bilan findByDemande(Long idDemande) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Bilan> q = em.createQuery(
                "SELECT b FROM Bilan b WHERE b.demande.id = :idd", Bilan.class);
        q.setParameter("idd", idDemande);
        try {
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
