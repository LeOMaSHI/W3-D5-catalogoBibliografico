package main;

import DAO.*;
import ents.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("CatalogoPU");
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            boolean running = true;
            while (running) {
                System.out.println("\n---- MENU ----");
                System.out.println("1. Aggiungi libro");
                System.out.println("2. Aggiungi rivista");
                System.out.println("3. Aggiungi utente");
                System.out.println("4. Fai prestito");
                System.out.println("5. Cerca per ISBN");
                System.out.println("6. Prestiti attivi per numero tessera");
                System.out.println("7. Prestiti scaduti");
                System.out.println("0. Esci");
                System.out.print("Scelta: ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> aggiungiLibro();
                    case 2 -> aggiungiRivista();
                    case 3 -> aggiungiUtente();
                    case 4 -> effettuaPrestito();
                    case 5 -> ricercaPerIsbn();
                    case 6 -> ricercaPrestitiPerTessera();
                    case 7 -> ricercaPrestitiScaduti();
                    case 0 -> {
                        running = false;
                        emf.close();
                        System.out.println("Programma terminato.");
                    }
                    default -> System.out.println("Scelta non valida.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void aggiungiLibro() {
        EntityManager em = emf.createEntityManager();
        ElementoCatalogoDAO dao = new ElementoCatalogoDAO(em);
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Titolo: ");
        String titolo = scanner.nextLine();
        System.out.print("Anno pubblicazione: ");
        int anno = Integer.parseInt(scanner.nextLine());
        System.out.print("Numero pagine: ");
        int pagine = Integer.parseInt(scanner.nextLine());
        System.out.print("Autore: ");
        String autore = scanner.nextLine();
        System.out.print("Genere: ");
        String genere = scanner.nextLine();

        Libro libro = new Libro(isbn, titolo, anno, pagine, autore, genere);
        dao.save(libro);
        em.close();
    }

    private static void aggiungiRivista() {
        EntityManager em = emf.createEntityManager();
        ElementoCatalogoDAO dao = new ElementoCatalogoDAO(em);
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Titolo: ");
        String titolo = scanner.nextLine();
        System.out.print("Anno pubblicazione: ");
        int anno = Integer.parseInt(scanner.nextLine());
        System.out.print("Numero pagine: ");
        int pagine = Integer.parseInt(scanner.nextLine());
        System.out.print("Periodicità (SETTIMANALE, MENSILE, SEMESTRALE): ");
        Periodicita periodicita = Periodicita.valueOf(scanner.nextLine().toUpperCase());

        Rivista rivista = new Rivista(isbn, titolo, anno, pagine, periodicita);
        dao.save(rivista);
        em.close();
    }

    private static void aggiungiUtente() {
        EntityManager em = emf.createEntityManager();
        UtenteDAO dao = new UtenteDAO(em);
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Cognome: ");
        String cognome = scanner.nextLine();
        System.out.print("Data di nascita (AAAA-MM-GG): ");
        LocalDate nascita = LocalDate.parse(scanner.nextLine());
        System.out.print("Numero tessera: ");
        String numeroTessera = scanner.nextLine();

        Utente u = new Utente( nome, cognome, nascita, numeroTessera);
        dao.save(u);
        em.close();
    }

    private static void effettuaPrestito() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            PrestitoDAO prestitoDAO = new PrestitoDAO(em);
            UtenteDAO utenteDAO = new UtenteDAO(em);
            ElementoCatalogoDAO catalogoDAO = new ElementoCatalogoDAO(em);

            System.out.print("Numero tessera utente: ");
            String tessera = scanner.nextLine();
            Utente utente = utenteDAO.findByNumeroTessera(tessera);
            if (utente == null) {
                System.out.println("Utente non trovato.");
                return;
            }

            System.out.print("ISBN dell'elemento da prendere in prestito: ");
            String isbn = scanner.nextLine();
            ElementoCatalogo elemento = catalogoDAO.findByISBN(isbn);
            if (elemento == null) {
                System.out.println("Elemento non trovato.");
                return;
            }

            Prestito prestito = new Prestito(utente, elemento, LocalDate.now());
            prestitoDAO.save(prestito);
            em.getTransaction().commit();
            System.out.println("Prestito creato con successo: " + prestito);
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void ricercaPerIsbn() {
        EntityManager em = emf.createEntityManager();
        ElementoCatalogoDAO dao = new ElementoCatalogoDAO(em);
        System.out.print("ISBN da cercare: ");
        String isbn = scanner.nextLine();
        ElementoCatalogo el = dao.findByISBN(isbn);
        if (el != null) {
            System.out.println("Trovato: " + el);
        } else {
            System.out.println("Nessun elemento trovato.");
        }
        em.close();
    }

    private static void ricercaPrestitiPerTessera() {
        EntityManager em = emf.createEntityManager();
        try {
            PrestitoDAO dao = new PrestitoDAO(em);
            UtenteDAO utenteDAO = new UtenteDAO(em);
            System.out.print("Numero tessera: ");
            String tessera = scanner.nextLine();
            
            Utente utente = utenteDAO.findByNumeroTessera(tessera);
            if (utente == null) {
                System.out.println("Utente non trovato con tessera: " + tessera);
                return;
            }
            System.out.println("Utente trovato: " + utente);

            List<Prestito> prestiti = dao.findPrestitiAttiviByTessera(tessera);
            if (prestiti.isEmpty()) {
                System.out.println("Nessun prestito attivo trovato per questa tessera.");
            } else {
                System.out.println("Prestiti attivi trovati:");
                prestiti.forEach(System.out::println);
            }
        } finally {
            em.close();
        }
    }

    private static void ricercaPrestitiScaduti() {
        EntityManager em = emf.createEntityManager();
        try {
            PrestitoDAO dao = new PrestitoDAO(em);
            List<Prestito> scaduti = dao.findPrestitiScaduti();
            if (scaduti.isEmpty()) {
                System.out.println("Non ci sono prestiti scaduti.");
            } else {
                System.out.println("Prestiti scaduti trovati:");
                scaduti.forEach(prestito -> {
                    System.out.println("ID: " + prestito.getId());
                    System.out.println("Utente: " + prestito.getUtente().getNome() + " " + prestito.getUtente().getCognome());
                    System.out.println("Elemento: " + prestito.getElementoPrestato().getTitolo());
                    System.out.println("Data inizio prestito: " + prestito.getDataInizioPrestito());
                    System.out.println("Data restituzione prevista: " + prestito.getDataRestituzionePrevista());
                    System.out.println("--------------------");
                });
            }
        } finally {
            em.close();
        }
    }
}