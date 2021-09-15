package fr.dranse.myapp.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Livre.
 */
@Entity
@Table(name = "livre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "livre")
public class Livre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "titre")
    private String titre;

    @Column(name = "auteur")
    private String auteur;

    @Column(name = "prix")
    private Float prix;

    @Column(name = "synopsis")
    private String synopsis;

    @Column(name = "edition")
    private Integer edition;

    @Column(name = "annee_publication")
    private Integer anneePublication;

    @Column(name = "editeur")
    private String editeur;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "url_image")
    private String urlImage;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_livre__livre_cat",
        joinColumns = @JoinColumn(name = "livre_id"),
        inverseJoinColumns = @JoinColumn(name = "livre_cat_id")
    )
    private Set<Categorie> livre_cats = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Livre id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitre() {
        return this.titre;
    }

    public Livre titre(String titre) {
        this.titre = titre;
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return this.auteur;
    }

    public Livre auteur(String auteur) {
        this.auteur = auteur;
        return this;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public Float getPrix() {
        return this.prix;
    }

    public Livre prix(Float prix) {
        this.prix = prix;
        return this;
    }

    public void setPrix(Float prix) {
        this.prix = prix;
    }

    public String getSynopsis() {
        return this.synopsis;
    }

    public Livre synopsis(String synopsis) {
        this.synopsis = synopsis;
        return this;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public Integer getEdition() {
        return this.edition;
    }

    public Livre edition(Integer edition) {
        this.edition = edition;
        return this;
    }

    public void setEdition(Integer edition) {
        this.edition = edition;
    }

    public Integer getAnneePublication() {
        return this.anneePublication;
    }

    public Livre anneePublication(Integer anneePublication) {
        this.anneePublication = anneePublication;
        return this;
    }

    public void setAnneePublication(Integer anneePublication) {
        this.anneePublication = anneePublication;
    }

    public String getEditeur() {
        return this.editeur;
    }

    public Livre editeur(String editeur) {
        this.editeur = editeur;
        return this;
    }

    public void setEditeur(String editeur) {
        this.editeur = editeur;
    }

    public Integer getStock() {
        return this.stock;
    }

    public Livre stock(Integer stock) {
        this.stock = stock;
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getUrlImage() {
        return this.urlImage;
    }

    public Livre urlImage(String urlImage) {
        this.urlImage = urlImage;
        return this;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Set<Categorie> getLivre_cats() {
        return this.livre_cats;
    }

    public Livre livre_cats(Set<Categorie> categories) {
        this.setLivre_cats(categories);
        return this;
    }

    public Livre addLivre_cat(Categorie categorie) {
        this.livre_cats.add(categorie);
        return this;
    }

    public Livre removeLivre_cat(Categorie categorie) {
        this.livre_cats.remove(categorie);
        return this;
    }

    public void setLivre_cats(Set<Categorie> categories) {
        this.livre_cats = categories;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Livre)) {
            return false;
        }
        return id != null && id.equals(((Livre) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Livre{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", auteur='" + getAuteur() + "'" +
            ", prix=" + getPrix() +
            ", synopsis='" + getSynopsis() + "'" +
            ", edition=" + getEdition() +
            ", anneePublication=" + getAnneePublication() +
            ", editeur='" + getEditeur() + "'" +
            ", stock=" + getStock() +
            ", urlImage='" + getUrlImage() + "'" +
            "}";
    }
}
