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
 * A Utilisateur.
 */
@Entity
@Table(name = "utilisateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "utilisateur")
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "mail")
    private String mail;

    @Column(name = "mot_de_passe")
    private String motDePasse;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "adr_rue")
    private String adrRue;

    @Column(name = "adr_code_postal")
    private Integer adrCodePostal;

    @Column(name = "adr_pays")
    private String adrPays;

    @Column(name = "adr_ville")
    private String adrVille;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "num_cb")
    private String numCB;

    @OneToMany(mappedBy = "utilisateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "utilisateur", "ligneCommandes" }, allowSetters = true)
    private Set<Commande> commandes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Utilisateur id(Long id) {
        this.id = id;
        return this;
    }

    public String getMail() {
        return this.mail;
    }

    public Utilisateur mail(String mail) {
        this.mail = mail;
        return this;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMotDePasse() {
        return this.motDePasse;
    }

    public Utilisateur motDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
        return this;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getNom() {
        return this.nom;
    }

    public Utilisateur nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Utilisateur prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdrRue() {
        return this.adrRue;
    }

    public Utilisateur adrRue(String adrRue) {
        this.adrRue = adrRue;
        return this;
    }

    public void setAdrRue(String adrRue) {
        this.adrRue = adrRue;
    }

    public Integer getAdrCodePostal() {
        return this.adrCodePostal;
    }

    public Utilisateur adrCodePostal(Integer adrCodePostal) {
        this.adrCodePostal = adrCodePostal;
        return this;
    }

    public void setAdrCodePostal(Integer adrCodePostal) {
        this.adrCodePostal = adrCodePostal;
    }

    public String getAdrPays() {
        return this.adrPays;
    }

    public Utilisateur adrPays(String adrPays) {
        this.adrPays = adrPays;
        return this;
    }

    public void setAdrPays(String adrPays) {
        this.adrPays = adrPays;
    }

    public String getAdrVille() {
        return this.adrVille;
    }

    public Utilisateur adrVille(String adrVille) {
        this.adrVille = adrVille;
        return this;
    }

    public void setAdrVille(String adrVille) {
        this.adrVille = adrVille;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Utilisateur telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getNumCB() {
        return this.numCB;
    }

    public Utilisateur numCB(String numCB) {
        this.numCB = numCB;
        return this;
    }

    public void setNumCB(String numCB) {
        this.numCB = numCB;
    }

    public Set<Commande> getCommandes() {
        return this.commandes;
    }

    public Utilisateur commandes(Set<Commande> commandes) {
        this.setCommandes(commandes);
        return this;
    }

    public Utilisateur addCommande(Commande commande) {
        this.commandes.add(commande);
        commande.setUtilisateur(this);
        return this;
    }

    public Utilisateur removeCommande(Commande commande) {
        this.commandes.remove(commande);
        commande.setUtilisateur(null);
        return this;
    }

    public void setCommandes(Set<Commande> commandes) {
        if (this.commandes != null) {
            this.commandes.forEach(i -> i.setUtilisateur(null));
        }
        if (commandes != null) {
            commandes.forEach(i -> i.setUtilisateur(this));
        }
        this.commandes = commandes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilisateur)) {
            return false;
        }
        return id != null && id.equals(((Utilisateur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + getId() +
            ", mail='" + getMail() + "'" +
            ", motDePasse='" + getMotDePasse() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", adrRue='" + getAdrRue() + "'" +
            ", adrCodePostal=" + getAdrCodePostal() +
            ", adrPays='" + getAdrPays() + "'" +
            ", adrVille='" + getAdrVille() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", numCB='" + getNumCB() + "'" +
            "}";
    }
}
