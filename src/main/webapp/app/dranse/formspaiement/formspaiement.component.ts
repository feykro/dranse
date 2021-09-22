import { CommandeControllerRessourceService } from './../service/commande-controller-ressource.service';
import { UtilisateurControllerRessourceService } from './../service/utilisateur-controller-ressource.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { PanierService } from '../panier/panier.service';
import { ICommande } from 'app/entities/commande/commande.model';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { IUser } from 'app/entities/user/user.model';
import { ILigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';

@Component({
  selector: 'jhi-formspaiement',
  templateUrl: './formspaiement.component.html',
  styleUrls: ['./formspaiement.component.scss'],
})
export class FormspaiementComponent implements OnInit {
  public commande!: ICommande;
  public utilisateur!: IUtilisateur;

  public nomLivraison = '';
  public adresseLivraison = '';
  public villeLivraison = '';
  public cpLivraison = '';
  public paysLivraison = '';

  public nomFacturation = '';
  public adresseFacturation = '';
  public villeFacturation = '';
  public paysFacturation = '';
  public cpFacturation = '';

  public numCB = '';
  public dateExpiration = '';
  public cryptogramme = '';

  public prixTotal = 0;

  public livraison!: boolean;
  public facturation!: boolean;
  public paiement!: boolean;

  public livraisonDone!: boolean;
  public facturationDone!: boolean;
  public paiementDone!: boolean;

  constructor(
    private panierService: PanierService,
    private commandeService: CommandeControllerRessourceService,
    private router: Router,
    private utilisateurService: UtilisateurControllerRessourceService
  ) {
    this.livraison = false;
    this.facturation = true;
    this.paiement = true;

    this.livraisonDone = false;
    this.facturationDone = false;
    this.paiementDone = false;
  }

  ngOnInit(): void {
    if (this.panierService.getPanierId() === -1) {
      this.router.navigate(['/404']);
    }

    // GET COMMANDE
    const commandeRequest: Observable<HttpResponse<ICommande>> = <Observable<HttpResponse<ICommande>>>(
      this.commandeService.getCommande(this.panierService.getPanierId())
    );
    commandeRequest.subscribe(value => {
      this.commande = <ICommande>value.body;
      let prix = 0;
      for (const item of <ILigneCommande[]>this.commande.ligneCommandes) {
        const price = item.livre?.prix === null || item.livre?.prix === undefined ? 0 : item.livre.prix;
        const qte = item.quantite === null || item.quantite === undefined ? 0 : item.quantite;
        prix += price * qte;
      }
      this.prixTotal = prix;
      this.prixTotal = +this.prixTotal.toFixed(2);
    });

    // GET UTILISATEUR
    const utilisateurRequest: Observable<HttpResponse<IUtilisateur>> = <Observable<HttpResponse<IUtilisateur>>>(
      this.utilisateurService.utilisateurCourrant()
    );
    utilisateurRequest.subscribe(value => {
      this.utilisateur = <IUtilisateur>value.body;

      // LIVRAISON
      this.nomLivraison = <string>(<IUser>this.utilisateur.userP).lastName;
      this.adresseLivraison = <string>this.utilisateur.adrRue;
      this.villeLivraison = <string>this.utilisateur.adrVille;
      this.paysLivraison = <string>this.utilisateur.adrPays;
      if (this.utilisateur.adrCodePostal !== undefined) {
        this.cpLivraison = (<number>this.utilisateur.adrCodePostal).toString();
      }
      // FACTURATION
      this.nomFacturation = <string>(<IUser>this.utilisateur.userP).lastName;
      this.adresseFacturation = <string>this.utilisateur.adrRue;
      this.villeFacturation = <string>this.utilisateur.adrVille;
      this.paysFacturation = <string>this.utilisateur.adrPays;
      if (this.utilisateur.adrCodePostal !== undefined) {
        this.cpFacturation = (<number>this.utilisateur.adrCodePostal).toString();
      }
      // PAIEMENT
      this.numCB = <string>this.utilisateur.numCB;
      console.log(this.utilisateur);
    });
  }

  submit1(): void {
    const regexCP = /\d{5}/g;
    const regexNomPrenom = /[a-zA-Z]+/g;
    if (regexCP.test(this.cpLivraison) && this.cpLivraison.length === 5) {
      if (regexNomPrenom.test(this.nomLivraison)) {
        this.commande.nomLivraison = this.nomLivraison;
        this.commande.rueLivraison = this.adresseLivraison;
        this.commande.villeLivraison = this.villeLivraison;
        this.commande.paysLivraison = this.paysLivraison;
        this.commande.codePostalLivraison = this.cpLivraison;
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
    const regexNomPrenom = /[a-zA-Z]+/g;
    if (regexCP.test(this.cpFacturation) && this.cpFacturation.length === 5) {
      if (regexNomPrenom.test(this.nomFacturation)) {
        this.commande.codePostalFacturation = this.cpFacturation;
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
    const regexcrypto = /\d{3}/g;
    if (regexnumCB.test(this.numCB) && this.numCB.replace(/\s/g, '').length === 16) {
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
          setTimeout(() => {
            this.router.navigate(['']);
          }, 5000);
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
