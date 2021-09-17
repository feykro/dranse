import { Validators } from '@angular/forms';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { PanierService } from '../panier/panier.service';
import { ICommande } from 'app/entities/commande/commande.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';

@Component({
  selector: 'jhi-formspaiement',
  templateUrl: './formspaiement.component.html',
  styleUrls: ['./formspaiement.component.scss'],
})
export class FormspaiementComponent implements OnInit {
  public commande!: ICommande;
  public utilisateur!: IUtilisateur;

  public nomLivraison = '';
  public prenomLivraison = '';
  public adresseLivraison = '';
  public villeLivraison = '';
  public cpLivraison = '';
  public paysLivraison = '';

  public nomFacturation = '';
  public prenomFacturation = '';
  public societe = '';
  public adresseFacturation = '';
  public villeFacturation = '';
  public paysFacturation = '';
  public cpFacturation = '';

  constructor(private panierService: PanierService, private router: Router, private utilisateurService: UtilisateurService) {}

  ngOnInit(): void {
    if (this.panierService.getPanierId() === -1) {
      // this.router.navigate(['/404']);
    }
    this.commande = this.panierService.getCommande();
    this.utilisateur;
  }

  submit1(): void {
    this.commande.nomLivraison = this.nomLivraison.concat(' ', this.prenomLivraison);
    this.commande.rueLivraison = this.adresseLivraison;
    this.commande.villeLivraison = this.villeLivraison;
    this.commande.paysLivraison = this.paysLivraison;
    const regex = new RegExp('d*');
    if (regex.test(this.cpLivraison)) {
      this.commande.codePostalLivraison = parseInt(this.cpLivraison);
    } else {
      alert('Le code postal ne doit etre composer que de chiffres');
    }
  }

  submit2(): void {
    // CORE
  }

  submit3(): void {
    // CORE
  }
}
