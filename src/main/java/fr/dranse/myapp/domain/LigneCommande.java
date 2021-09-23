package fr.dranse.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A LigneCommande.
 */
@Entity
@Table(name = "ligne_commande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "lignecommande")
public class LigneCommande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "quantite")
    private Integer quantite;

    @Column(name = "prix_paye")
    private Float prixPaye;

    @ManyToOne
    @JsonIgnoreProperties(value = {"utilisateur", "ligneCommandes"}, allowSetters = true)
    private Commande commande;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties(value = {"livre_cats"}, allowSetters = true)
    private Livre livre;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LigneCommande id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public LigneCommande quantite(Integer quantite) {
        this.quantite = quantite;
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Float getPrixPaye() {
        return this.prixPaye;
    }

    public LigneCommande prixPaye(Float prixPaye) {
        this.prixPaye = prixPaye;
        return this;
    }

    public void setPrixPaye(Float prixPaye) {
        this.prixPaye = prixPaye;
    }

    public Commande getCommande() {
        return this.commande;
    }

    public LigneCommande commande(Commande commande) {
        this.setCommande(commande);
        return this;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Livre getLivre() {
        return this.livre;
    }

    public LigneCommande livre(Livre livre) {
        this.setLivre(livre);
        return this;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LigneCommande)) {
            return false;
        }
        return id != null && id.equals(((LigneCommande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LigneCommande{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", prixPaye=" + getPrixPaye() +
            "}";
    }

    public void setLivreQuantite(Livre livre, int quantite) {
        this.setLivre(livre);
        this.setQuantite(quantite);
        this.setPrixPaye(livre.getPrix() * quantite);
    }

    public void updateQuantite(int quantite) {
        setQuantite(quantite);
        if (getLivre() != null) {
            setPrixPaye(getLivre().getPrix() * quantite);
        }
    }
}
