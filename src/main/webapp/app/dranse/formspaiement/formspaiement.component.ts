import { UtilisateurControllerRessourceService } from './../service/utilisateur-controller-ressource.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { PanierService } from '../panier/panier.service';
import { ICommande } from 'app/entities/commande/commande.model';

@Component({
  selector: 'jhi-formspaiement',
  templateUrl: './formspaiement.component.html',
  styleUrls: ['./formspaiement.component.scss'],
})
export class FormspaiementComponent implements OnInit {
  public commande!: ICommande;
  public utilisateur!: IUtilisateur;

  public nomLivraison: string;
  public adresseLivraison: string;
  public villeLivraison: string;
  public cpLivraison: string;
  public paysLivraison: string;

  public nomFacturation: string;
  public adresseFacturation: string;
  public villeFacturation: string;
  public paysFacturation: string;
  public cpFacturation: string;

  public numCB: string;
  public dateExpiration: string;
  public cryptogramme: string;

  public livraison!: boolean;
  public facturation!: boolean;
  public paiement!: boolean;

  public livraisonDone!: boolean;
  public facturationDone!: boolean;
  public paiementDone!: boolean;

  constructor(
    private panierService: PanierService,
    private router: Router,
    private utilisateurService: UtilisateurControllerRessourceService
  ) {
    this.livraison = false;
    this.facturation = true;
    this.paiement = true;

    this.livraisonDone = false;
    this.facturationDone = false;
    this.paiementDone = false;

    this.utilisateur = <IUtilisateur>utilisateurService.utilisateurCourrant();

    // LIVRAISON
    this.nomLivraison = 'Nom'; // <string>this.utilisateur;
    this.adresseLivraison = <string>this.utilisateur.adrRue;
    this.villeLivraison = <string>this.utilisateur.adrVille;
    this.paysLivraison = <string>this.utilisateur.adrPays;
    this.cpLivraison = (<number>this.utilisateur.adrCodePostal).toString();

    // FACTURATION
    this.nomFacturation = 'Nom'; // <string>this.utilisateur;
    this.adresseFacturation = <string>this.utilisateur.adrRue;
    this.villeFacturation = <string>this.utilisateur.adrVille;
    this.paysFacturation = <string>this.utilisateur.adrPays;
    this.cpFacturation = (<number>this.utilisateur.adrCodePostal).toString();

    // PAIEMENT
    this.numCB = <string>this.utilisateur.numCB;
    this.cryptogramme = '';
    this.dateExpiration = '';
  }

  ngOnInit(): void {
    if (this.panierService.getPanierId() === -1) {
      // this.router.navigate(['/404']);
    }
    this.commande = this.panierService.getCommande();
  }

  submit1(): void {
    const regexCP = /\d{5}/g;
    const regexNomPrenom = /[a-zA-Z]+\s*[a-zA-Z]*/g;
    if (regexCP.test(this.cpLivraison.toString()) === false) {
      if (regexNomPrenom.test(this.nomLivraison)) {
        this.commande.nomLivraison = this.nomLivraison;
        this.commande.rueLivraison = this.adresseLivraison;
        this.commande.villeLivraison = this.villeLivraison;
        this.commande.paysLivraison = this.paysLivraison;
        this.commande.codePostalLivraison = parseInt(this.cpLivraison.toString(), 10);
        this.livraisonDone = true;
        this.showFacturation();
      } else {
        alert('Le Nom et Prénom ne sont composés que de lettres');
      }
    } else {
      alert('Le code postal ne doit etre composer que de 5 chiffres');
    }
  }

  submit2(): void {
    const regexCP = /\d{5}/g;
    const regexNomPrenom = /[a-zA-Z]+\s*[a-zA-Z]*/g;
    if (regexCP.test(this.cpFacturation)) {
      if (regexNomPrenom.test(this.nomFacturation)) {
        // this.commande.codePostalFacturation = parseInt(this.cpFacturation,10);
        this.commande.nomFacturation = this.nomFacturation;
        this.commande.rueFacturation = this.adresseFacturation;
        this.commande.villeFacturation = this.villeFacturation;
        this.commande.paysFacturation = this.paysFacturation;
        this.facturationDone = true;
        this.showPaiement();
      } else {
        alert('Le Nom et Prénom ne sont composés que de lettres');
      }
    } else {
      alert('Le code postal ne doit etre composer que de 5 chiffres');
    }
  }

  submit3(): void {
    const regexnumCB = /(\d{16})|(\d{4}\s\d{4}\s\d{4}\s\d{4})/g;
    const regexdateExp = /\d{2}\/\d{2}/g;
    const regexcrypto = /\d{2,3}/g;
    console.log(this.numCB);
    console.log(this.dateExpiration);
    console.log(this.cryptogramme);
    if (regexnumCB.test(this.numCB)) {
      if (regexdateExp.test(this.dateExpiration)) {
        if (regexcrypto.test(this.cryptogramme)) {
          this.paiementDone = true;
          this.paiement = true;
        } else {
          alert("Le cryptogramme n'est pas valable (ex : 123)");
        }
      } else {
        alert("La date n'est pas valable (mm/aa)");
      }
    } else {
      alert("Le numéro de carte n'est pas valable (ex : 0000 1111 2222 3333)");
    }
  }

  validationAll(): void {
    if (this.livraisonDone === false || this.facturationDone === false || this.paiementDone === false) {
      if (this.livraisonDone === false) {
        this.showLivraison();
        alert('Faire le formulaire Livraison avant de valider');
      } else {
        if (this.facturationDone === false) {
          this.showFacturation();
          alert('Faire le formulaire Facuration avant de valider');
        } else {
          if (this.paiementDone === false) {
            this.showPaiement();
            alert('Faire le paiement Facuration avant de valider');
          } else {
            this.commande.utilisateur = this.utilisateur;
            this.panierService.passerCommande(this.commande);
            this.panierService.clearId();
          }
        }
      }
    }
  }

  showLivraison(): void {
    this.livraison = false;
    this.facturation = true;
    this.paiement = true;
  }

  showFacturation(): void {
    this.livraison = true;
    this.facturation = false;
    this.paiement = true;
  }

  showPaiement(): void {
    this.livraison = true;
    this.facturation = true;
    this.paiement = false;
  }
}
