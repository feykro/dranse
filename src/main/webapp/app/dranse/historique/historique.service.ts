import { Injectable } from '@angular/core';
import { ICommande } from 'app/entities/commande/commande.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ILigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';
import { CommandeControllerRessourceService } from './../service/commande-controller-ressource.service';
import { UtilisateurControllerRessourceService } from './../service/utilisateur-controller-ressource.service';


@Injectable({
  providedIn: 'root'
})
export class HistoriqueService {

  public historique !: any[];

  constructor(
    private commandeService: CommandeControllerRessourceService,
    private utilisateurService: UtilisateurControllerRessourceService) {
  //not empty
   }


  public getHistoriqueId() : number{
      const id = <number | null>JSON.parse(<string>localStorage.getItem('historiqueId'));
        if (id === null) {
          return -1;
        }
        return id;
  }

  getHistorique(): Observable<HttpResponse<ICommande[]>> {

        const historiqueRequest:  Observable<HttpResponse<ICommande[]>> = <Observable<HttpResponse<ICommande[]>>>(
          this.commandeService.getHistory()
        );
        return historiqueRequest;
      }

}
