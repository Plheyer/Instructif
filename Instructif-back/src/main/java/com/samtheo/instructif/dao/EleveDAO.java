package com.samtheo.instructif.dao;

import com.samtheo.instructif.metier.modele.Eleve;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class EleveDAO {

    public void create(Eleve eleve) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(eleve);
    }

    public Eleve findById(Long id) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.find(Eleve.class, id);
    }

    public Eleve findByEmail(String email) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Eleve> q = em.createQuery(
                "SELECT e FROM Eleve e WHERE e.email = :email", Eleve.class);
        q.setParameter("email", email);
        try {
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Eleve authentifier(String email, String motDePasse) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Eleve> q = em.createQuery(
                "SELECT e FROM Eleve e WHERE e.email = :email AND e.motDePasse = :mdp",
                Eleve.class);
        q.setParameter("email", email);
        q.setParameter("mdp", motDePasse);
        try {
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public List<Eleve> findAll() {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.createQuery(
                "SELECT e FROM Eleve e ORDER BY e.nom ASC, e.prenom ASC",
                Eleve.class).getResultList();
    }
}
