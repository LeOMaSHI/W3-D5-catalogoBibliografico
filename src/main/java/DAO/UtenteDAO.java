package DAO;

import ents.Utente;
import jakarta.persistence.NoResultException;
import util.JPAUtil;
import jakarta.persistence.EntityManager;

public class UtenteDAO {
    private EntityManager em;

    public UtenteDAO(EntityManager em) {
        this.em = em;
    }

    public void save(Utente utente) {
        try {
            em.getTransaction().begin();
            em.persist(utente);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public Utente findByNumeroTessera(String tessera) {
        try {
            return em.createQuery("SELECT u FROM Utente u WHERE u.numeroTessera = :tessera", Utente.class)
                    .setParameter("tessera", tessera)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}