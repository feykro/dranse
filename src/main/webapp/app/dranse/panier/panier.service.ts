import { ILigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';
import { CommandeControllerRessourceService } from './../service/commande-controller-ressource.service';
import { Injectable } from '@angular/core';
import { ICommande } from 'app/entities/commande/commande.model';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class PanierService {
  public commande!: ICommande;
  private lignes!: ILigneCommande[];

  constructor(private commandeService: CommandeControllerRessourceService) {
    // Not empty
  }

  getPanierId(): number {
    const id = <number | null>JSON.parse(<string>localStorage.getItem('panierId'));
    if (id === null) {
      return -1;
    }
    return id;
  }

  setPanierId(id: number): void {
    localStorage.setItem('panierId', JSON.stringify(id));
  }

  clearId(): void {
    localStorage.removeItem('panierId');
  }

  getCommande(): ICommande {
    const commandeRequest: Observable<HttpResponse<ICommande>> = <Observable<HttpResponse<ICommande>>>(
      this.commandeService.getCommande(this.getPanierId())
    );
    commandeRequest.subscribe(value => {
      if(value.body === null){
        console.log("nooooooooooooooooooo\n\n\n")
      }
      this.commande = <ICommande>value.body;
    });
    return this.commande;
  }

  creationCommande(ligneCommande: ILigneCommande): void {
    const commandeRequest: Observable<HttpResponse<ICommande>> = <Observable<HttpResponse<ICommande>>>(
      this.commandeService.creationCommande(ligneCommande)
    );
    commandeRequest.subscribe(value => {
      if (value.body === null) {
        alert("Le livre n'a pas été trouvé ou n'est plus en stock");
      } else {
        this.commande = value.body;
        localStorage.setItem('panierId', JSON.stringify(<number>value.body.id));
        confirm('Le produit a été ajouté au panier');
      }
    });
  }

  ajoutLigne(ligneCommande: ILigneCommande): void {
    const commandeRequest: Observable<HttpResponse<ICommande>> = <Observable<HttpResponse<ICommande>>>(
      this.commandeService.ajoutLigneCommande(ligneCommande, this.getPanierId())
    );
    commandeRequest.subscribe(value => {
      if (value.body === null) {
        alert("Le livre n'a pas été trouvé ou n'est plus en stock");
      } else {
        this.commande = value.body;
        confirm('Le produit a été ajouté au panier');
      }
    });
  }

  passerCommande(commande: ICommande): void {
    const commandeRequest: Observable<HttpResponse<ICommande>> = <Observable<HttpResponse<ICommande>>>(
      this.commandeService.passerCommande(this.getPanierId(), commande)
    );
    commandeRequest.subscribe();
  }

  modifierLigne(ligneCommande: ILigneCommande): Observable<HttpResponse<ICommande>> {
    const commandeRequest: Observable<HttpResponse<ICommande>> = <Observable<HttpResponse<ICommande>>>(
      this.commandeService.modifierLigneCommande(ligneCommande, this.getPanierId())
    );

    return commandeRequest;
  }
}
