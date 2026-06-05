package com.samtheo.instructif.dao;

import com.samtheo.instructif.metier.modele.Demande;
import com.samtheo.instructif.metier.modele.Statut;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class DemandeDAO {

    public void create(Demande demande) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.persist(demande);
    }

    public void update(Demande demande) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        em.merge(demande);
    }

    public Demande findById(Long id) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        return em.find(Demande.class, id);
    }

    public List<Demande> findByEleve(Long idEleve) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Demande> q = em.createQuery(
                "SELECT d FROM Demande d WHERE d.eleve.id = :ide " +
                "ORDER BY d.dateHeureDebut DESC, d.id DESC",
                Demande.class);
        q.setParameter("ide", idEleve);
        return q.getResultList();
    }

    public List<Demande> findRecentesByEleve(Long idEleve, int n) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Demande> q = em.createQuery(
                "SELECT d FROM Demande d WHERE d.eleve.id = :ide " +
                "ORDER BY d.dateHeureDebut DESC, d.id DESC",
                Demande.class);
        q.setParameter("ide", idEleve);
        q.setMaxResults(n);
        return q.getResultList();
    }

    public List<Demande> findByIntervenant(Long idIntervenant) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Demande> q = em.createQuery(
                "SELECT d FROM Demande d WHERE d.intervenant.id = :idi " +
                "ORDER BY d.dateHeureDebut DESC, d.id DESC",
                Demande.class);
        q.setParameter("idi", idIntervenant);
        return q.getResultList();
    }

    public Demande findEnCoursByIntervenant(Long idIntervenant) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Demande> q = em.createQuery(
                "SELECT d FROM Demande d WHERE d.intervenant.id = :idi " +
                "AND d.statut = :st ORDER BY d.dateHeureDebut DESC",
                Demande.class);
        q.setParameter("idi", idIntervenant);
        q.setParameter("st", Statut.EN_COURS);
        q.setMaxResults(1);
        List<Demande> res = q.getResultList();
        return res.isEmpty() ? null : res.get(0);
    }

    public List<Demande> findByStatut(Statut statut) {
        EntityManager em = JpaUtil.obtenirContextePersistance();
        TypedQuery<Demande> q = em.createQuery(
                "SELECT d FROM Demande d WHERE d.statut = :st",
                Demande.class);
        q.setParameter("st", statut);
        return q.getResultList();
    }
}
