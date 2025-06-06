package DAO;

import ents.Prestito;
import util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

public class PrestitoDAO {

    public PrestitoDAO(EntityManager em) {
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
    }

    public List<Prestito> findPrestitiAttiviByTessera(String tessera) {
        return List.of();
    }

    public List<Prestito> findPrestitiScaduti() {
        return List.of();
    }
}
