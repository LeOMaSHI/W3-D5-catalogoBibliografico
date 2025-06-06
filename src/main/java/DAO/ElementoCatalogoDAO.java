package DAO;

import ents.ElementoCatalogo;
import ents.Libro;
import ents.Rivista;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ElementoCatalogoDAO {
    private EntityManager em;

    public ElementoCatalogoDAO(EntityManager em) {
        this.em = em;
    }

    public void save(ElementoCatalogo elemento) {
        em.getTransaction().begin();
        em.persist(elemento);
        em.getTransaction().commit();
    }

    public void remove(String isbn) {
        em.getTransaction().begin();
        ElementoCatalogo e = em.find(ElementoCatalogo.class, isbn);
        if (e != null) em.remove(e);
        em.getTransaction().commit();
    }

    public ElementoCatalogo findByISBN(String isbn) {
        return em.find(ElementoCatalogo.class, isbn);
    }

    public List<ElementoCatalogo> findByAnno(int anno) {
        return em.createQuery("SELECT e FROM ElementoCatalogo e WHERE e.annoPubblicazione = :anno", ElementoCatalogo.class)
                .setParameter("anno", anno)
                .getResultList();
    }

    public List<Libro> findByAutore(String autore) {
        return em.createQuery("SELECT l FROM Libro l WHERE LOWER(l.autore) LIKE LOWER(:autore)", Libro.class)
                .setParameter("autore", "%" + autore + "%")
                .getResultList();
    }

    public List<ElementoCatalogo> findByTitolo(String titolo) {
        return em.createQuery("SELECT e FROM ElementoCatalogo e WHERE LOWER(e.titolo) LIKE LOWER(:titolo)", ElementoCatalogo.class)
                .setParameter("titolo", "%" + titolo + "%")
                .getResultList();
    }
}

