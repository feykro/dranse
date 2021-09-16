package fr.dranse.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Avis.
 */
@Entity
@Table(name = "avis")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "avis")
public class Avis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Min(value = 0)
    @Max(value = 10)
    @Column(name = "note")
    private Integer note;

    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "date_publication")
    private ZonedDateTime datePublication;

    @Column(name = "affiche")
    private Boolean affiche;

    @ManyToOne
    @JsonIgnoreProperties(value = { "livre_cats" }, allowSetters = true)
    private Livre livre;

    @ManyToOne
    @JsonIgnoreProperties(value = { "commandes", "user" }, allowSetters = true)
    private Utilisateur utilisateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Avis id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getNote() {
        return this.note;
    }

    public Avis note(Integer note) {
        this.note = note;
        return this;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public Avis commentaire(String commentaire) {
        this.commentaire = commentaire;
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public ZonedDateTime getDatePublication() {
        return this.datePublication;
    }

    public Avis datePublication(ZonedDateTime datePublication) {
        this.datePublication = datePublication;
        return this;
    }

    public void setDatePublication(ZonedDateTime datePublication) {
        this.datePublication = datePublication;
    }

    public Boolean getAffiche() {
        return this.affiche;
    }

    public Avis affiche(Boolean affiche) {
        this.affiche = affiche;
        return this;
    }

    public void setAffiche(Boolean affiche) {
        this.affiche = affiche;
    }

    public Livre getLivre() {
        return this.livre;
    }

    public Avis livre(Livre livre) {
        this.setLivre(livre);
        return this;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public Avis utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Avis)) {
            return false;
        }
        return id != null && id.equals(((Avis) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Avis{" +
            "id=" + getId() +
            ", note=" + getNote() +
            ", commentaire='" + getCommentaire() + "'" +
            ", datePublication='" + getDatePublication() + "'" +
            ", affiche='" + getAffiche() + "'" +
            "}";
    }
}
