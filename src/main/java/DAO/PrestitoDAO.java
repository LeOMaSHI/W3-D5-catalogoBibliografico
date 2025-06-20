package DAO;

import ents.Prestito;
import util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

public class PrestitoDAO {
    private EntityManager em;

    public PrestitoDAO(EntityManager em) {
        this.em = em;
    }

    public void salva(Prestito prestito) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(prestito);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Prestito> trovaPrestitiAttiviPerUtente(String numeroTessera) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Prestito p WHERE p.utente.numeroTessera = :num AND p.dataRestituzioneEffettiva IS NULL",
                            Prestito.class)
                    .setParameter("num", numeroTessera)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Prestito> trovaPrestitiScadutiNonRestituiti() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Prestito p WHERE p.dataRestituzioneEffettiva IS NULL AND p.dataRestituzionePrevista < :oggi",
                            Prestito.class)
                    .setParameter("oggi", LocalDate.now())
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void save(Prestito prestito) {
        em.persist(prestito);
    }

    public List<Prestito> findPrestitiAttiviByTessera(String tessera) {
        List<Prestito> prestiti = em.createQuery(
                "SELECT p FROM Prestito p WHERE p.utente.numeroTessera = :tessera AND p.dataRestituzioneEffettiva IS NULL",
                Prestito.class)
                .setParameter("tessera", tessera)
                .getResultList();
        System.out.println("Query executed. Found " + prestiti.size() + " active loans for tessera: " + tessera);
        return prestiti;
    }

    public List<Prestito> findPrestitiScaduti() {
        LocalDate today = LocalDate.now();
        return em.createQuery(
                "SELECT p FROM Prestito p WHERE p.dataRestituzionePrevista < :today AND p.dataRestituzioneEffettiva IS NULL",
                Prestito.class)
                .setParameter("today", today)
                .getResultList();
    }
}
