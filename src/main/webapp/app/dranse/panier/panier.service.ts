import { LigneCommande } from './../../entities/ligne-commande/ligne-commande.model';
import { CommandeControllerRessourceService } from './../service/commande-controller-ressource.service';
import { Injectable } from '@angular/core';
import { Commande } from 'app/entities/commande/commande.model';

@Injectable({
  providedIn: 'root',
})
export class PanierService {
  panierId = -1;
  commande!: Commande;

  constructor(private commandeService: CommandeControllerRessourceService) {
    //Not empty
  }

  getPanierId(): number {
    return this.panierId;
  }

  setPanierId(id: number): void {
    this.panierId = id;
  }

  getCommande(): Commande {
    this.commandeService.getCommande(this.panierId).subscribe;
    //Recuperer la commande
    return this.commande;
  }

  //Return True = Ok
  //Return False = Error
  creationCommande(ligneCommande: LigneCommande): boolean {
    this.commandeService.creationCommande(ligneCommande).subscribe();
    //TODO Changer la valeur du panierId
    return true;
  }

  //Return True = Ok
  //Return False = Error
  ajoutLigne(ligneCommande: LigneCommande): boolean {
    this.commandeService.ajoutLigneCommande(ligneCommande, this.panierId).subscribe();
    //Retourne la commande ou erreur
    return true;
  }
}
