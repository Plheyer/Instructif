package com.samtheo.instructif.dao;

import com.samtheo.instructif.metier.modele.Theme;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class ThemeDAO {

    public void create(Theme theme) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(theme);
    }

    public Theme findById(Long id) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.find(Theme.class, id);
    }

    public List<Theme> findByMatiere(Long idMatiere) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Theme> q = em.createQuery(
                "SELECT t FROM Theme t WHERE t.matiere.id = :idm ORDER BY t.intitule ASC",
                Theme.class);
        q.setParameter("idm", idMatiere);
        return q.getResultList();
    }
}
