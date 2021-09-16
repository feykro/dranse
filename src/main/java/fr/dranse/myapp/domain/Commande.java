package fr.dranse.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Commande.
 */
@Entity
@Table(name = "commande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "commande")
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "pays_livraison")
    private String paysLivraison;

    @Column(name = "code_postal_livraison")
    private Integer codePostalLivraison;

    @Column(name = "ville_livraison")
    private String villeLivraison;

    @Column(name = "rue_livraison")
    private String rueLivraison;

    @Column(name = "nom_livraison")
    private String nomLivraison;

    @Column(name = "pays_facturation")
    private String paysFacturation;

    @Column(name = "code_postal_facturation")
    private String codePostalFacturation;

    @Column(name = "ville_facturation")
    private String villeFacturation;

    @Column(name = "rue_facturation")
    private String rueFacturation;

    @Column(name = "nom_facturation")
    private String nomFacturation;

    @Column(name = "payee")
    private Boolean payee;

    @ManyToOne
    @JsonIgnoreProperties(value = { "commandes", "userP" }, allowSetters = true)
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "commande", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "commande" }, allowSetters = true)
    private Set<LigneCommande> ligneCommandes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Commande id(Long id) {
        this.id = id;
        return this;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Commande date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getPaysLivraison() {
        return this.paysLivraison;
    }

    public Commande paysLivraison(String paysLivraison) {
        this.paysLivraison = paysLivraison;
        return this;
    }

    public void setPaysLivraison(String paysLivraison) {
        this.paysLivraison = paysLivraison;
    }

    public Integer getCodePostalLivraison() {
        return this.codePostalLivraison;
    }

    public Commande codePostalLivraison(Integer codePostalLivraison) {
        this.codePostalLivraison = codePostalLivraison;
        return this;
    }

    public void setCodePostalLivraison(Integer codePostalLivraison) {
        this.codePostalLivraison = codePostalLivraison;
    }

    public String getVilleLivraison() {
        return this.villeLivraison;
    }

    public Commande villeLivraison(String villeLivraison) {
        this.villeLivraison = villeLivraison;
        return this;
    }

    public void setVilleLivraison(String villeLivraison) {
        this.villeLivraison = villeLivraison;
    }

    public String getRueLivraison() {
        return this.rueLivraison;
    }

    public Commande rueLivraison(String rueLivraison) {
        this.rueLivraison = rueLivraison;
        return this;
    }

    public void setRueLivraison(String rueLivraison) {
        this.rueLivraison = rueLivraison;
    }

    public String getNomLivraison() {
        return this.nomLivraison;
    }

    public Commande nomLivraison(String nomLivraison) {
        this.nomLivraison = nomLivraison;
        return this;
    }

    public void setNomLivraison(String nomLivraison) {
        this.nomLivraison = nomLivraison;
    }

    public String getPaysFacturation() {
        return this.paysFacturation;
    }

    public Commande paysFacturation(String paysFacturation) {
        this.paysFacturation = paysFacturation;
        return this;
    }

    public void setPaysFacturation(String paysFacturation) {
        this.paysFacturation = paysFacturation;
    }

    public String getCodePostalFacturation() {
        return this.codePostalFacturation;
    }

    public Commande codePostalFacturation(String codePostalFacturation) {
        this.codePostalFacturation = codePostalFacturation;
        return this;
    }

    public void setCodePostalFacturation(String codePostalFacturation) {
        this.codePostalFacturation = codePostalFacturation;
    }

    public String getVilleFacturation() {
        return this.villeFacturation;
    }

    public Commande villeFacturation(String villeFacturation) {
        this.villeFacturation = villeFacturation;
        return this;
    }

    public void setVilleFacturation(String villeFacturation) {
        this.villeFacturation = villeFacturation;
    }

    public String getRueFacturation() {
        return this.rueFacturation;
    }

    public Commande rueFacturation(String rueFacturation) {
        this.rueFacturation = rueFacturation;
        return this;
    }

    public void setRueFacturation(String rueFacturation) {
        this.rueFacturation = rueFacturation;
    }

    public String getNomFacturation() {
        return this.nomFacturation;
    }

    public Commande nomFacturation(String nomFacturation) {
        this.nomFacturation = nomFacturation;
        return this;
    }

    public void setNomFacturation(String nomFacturation) {
        this.nomFacturation = nomFacturation;
    }

    public Boolean getPayee() {
        return this.payee;
    }

    public Commande payee(Boolean payee) {
        this.payee = payee;
        return this;
    }

    public void setPayee(Boolean payee) {
        this.payee = payee;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public Commande utilisateur(Utilisateur utilisateur) {
        this.setUtilisateur(utilisateur);
        return this;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Set<LigneCommande> getLigneCommandes() {
        return this.ligneCommandes;
    }

    public Commande ligneCommandes(Set<LigneCommande> ligneCommandes) {
        this.setLigneCommandes(ligneCommandes);
        return this;
    }

    public Commande addLigneCommande(LigneCommande ligneCommande) {
        this.ligneCommandes.add(ligneCommande);
        ligneCommande.setCommande(this);
        return this;
    }

    public Commande removeLigneCommande(LigneCommande ligneCommande) {
        this.ligneCommandes.remove(ligneCommande);
        ligneCommande.setCommande(null);
        return this;
    }

    public void setLigneCommandes(Set<LigneCommande> ligneCommandes) {
        if (this.ligneCommandes != null) {
            this.ligneCommandes.forEach(i -> i.setCommande(null));
        }
        if (ligneCommandes != null) {
            ligneCommandes.forEach(i -> i.setCommande(this));
        }
        this.ligneCommandes = ligneCommandes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commande)) {
            return false;
        }
        return id != null && id.equals(((Commande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commande{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", paysLivraison='" + getPaysLivraison() + "'" +
            ", codePostalLivraison=" + getCodePostalLivraison() +
            ", villeLivraison='" + getVilleLivraison() + "'" +
            ", rueLivraison='" + getRueLivraison() + "'" +
            ", nomLivraison='" + getNomLivraison() + "'" +
            ", paysFacturation='" + getPaysFacturation() + "'" +
            ", codePostalFacturation='" + getCodePostalFacturation() + "'" +
            ", villeFacturation='" + getVilleFacturation() + "'" +
            ", rueFacturation='" + getRueFacturation() + "'" +
            ", nomFacturation='" + getNomFacturation() + "'" +
            ", payee='" + getPayee() + "'" +
            "}";
    }
}
