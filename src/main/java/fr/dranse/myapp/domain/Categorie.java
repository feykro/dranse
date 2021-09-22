package fr.dranse.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Categorie.
 */
@Entity
@Table(name = "categorie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "categorie")
public class Categorie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "livre_cats")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "livre_cats" }, allowSetters = true)
    private Set<Livre> livres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Categorie id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Categorie nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public Categorie description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Livre> getLivres() {
        return this.livres;
    }

    public Categorie livres(Set<Livre> Livres) {
        this.setLivres(Livres);
        return this;
    }

    public Categorie addLivre(Livre Livre) {
        this.livres.add(Livre);
        Livre.getLivre_cats().add(this);
        return this;
    }

    public Categorie removeLivre(Livre Livre) {
        System.out.println("We currently are removing \n" + Livre + "\n from \n" + this.livres);
        boolean n = this.livres.remove(Livre);
        System.out.println("\nHere is the resulting set :\n " + this.livres + " \nit was a\n " + n);
        Livre.getLivre_cats().remove(this);
        return this;
    }

    public void setLivres(Set<Livre> Livres) {
        if (this.livres != null) {
            this.livres.forEach(i -> i.removeLivre_cat(this));
        }
        if (Livres != null) {
            Livres.forEach(i -> i.addLivre_cat(this));
        }
        this.livres = Livres;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Categorie)) {
            return false;
        }
        return id != null && id.equals(((Categorie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Categorie{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
