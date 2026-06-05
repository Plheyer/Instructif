package com.samtheo.instructif.dao;

import com.samtheo.instructif.metier.modele.Matiere;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class MatiereDAO {

    public void create(Matiere matiere) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(matiere);
    }

    public Matiere findById(Long id) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.find(Matiere.class, id);
    }

    public Matiere findByNom(String nom) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Matiere> q = em.createQuery(
                "SELECT m FROM Matiere m WHERE m.nom = :nom", Matiere.class);
        q.setParameter("nom", nom);
        try {
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public List<Matiere> findAll() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.createQuery(
                "SELECT m FROM Matiere m ORDER BY m.nom ASC", Matiere.class)
                .getResultList();
    }
}
