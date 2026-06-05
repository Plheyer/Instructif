package com.samtheo.instructif.dao;

import com.samtheo.instructif.metier.modele.Etablissement;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class EtablissementDAO {

    public void create(Etablissement etablissement) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(etablissement);
    }

    public Etablissement findById(Long id) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.find(Etablissement.class, id);
    }

    public Etablissement findByCodeUAI(String codeUAI) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Etablissement> q = em.createQuery(
                "SELECT e FROM Etablissement e WHERE e.codeUAI = :uai",
                Etablissement.class);
        q.setParameter("uai", codeUAI);
        try {
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
