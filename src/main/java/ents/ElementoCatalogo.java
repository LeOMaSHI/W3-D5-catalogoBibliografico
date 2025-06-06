package ents;


import jakarta.persistence.*;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class ElementoCatalogo {
    @Id
    private String isbn;

    private String titolo;
    private int annoPubblicazione;
    private int numeroPagine;

    public ElementoCatalogo() {}

    public ElementoCatalogo(String isbn, String titolo, int anno, int pagine) {
        this.isbn = isbn;
        this.titolo = titolo;
        this.annoPubblicazione = anno;
        this.numeroPagine = pagine;
    }


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public int getAnnoPubblicazione() {
        return annoPubblicazione;
    }

    public void setAnnoPubblicazione(int annoPubblicazione) {
        this.annoPubblicazione = annoPubblicazione;
    }

    public int getNumeroPagine() {
        return numeroPagine;
    }

    public void setNumeroPagine(int numeroPagine) {
        this.numeroPagine = numeroPagine;
    }

    @Override
    public String toString() {
        return "ElementoCatalogo{" +
                "isbn='" + isbn + '\'' +
                ", titolo='" + titolo + '\'' +
                ", annoPubblicazione=" + annoPubblicazione +
                ", numeroPagine=" + numeroPagine +
                '}';
    }
}
