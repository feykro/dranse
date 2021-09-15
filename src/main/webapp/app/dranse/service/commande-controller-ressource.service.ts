import { getLigneCommandeIdentifier, LigneCommande } from './../../entities/ligne-commande/ligne-commande.model';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Commande, getCommandeIdentifier } from 'app/entities/commande/commande.model';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

export type EntityResponseType = HttpResponse<Commande>;
export type EntityArrayResponseType = HttpResponse<Commande[]>;

@Injectable({
  providedIn: 'root',
})
export class CommandeControllerRessourceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/v1/commande');

  constructor(private http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  creationCommande(ligneCommande: LigneCommande): Observable<EntityResponseType> {
    return this.http.post<Commande>(`${this.resourceUrl}`, ligneCommande, { observe: 'response' });
  }

  ajoutLigneCommande(ligneCommande: LigneCommande, id: number): Observable<EntityResponseType> {
    return this.http.post<Commande>(`${this.resourceUrl}/ajout/${id}`, ligneCommande, { observe: 'response' });
  }

  getCommande(id: number): Observable<EntityResponseType> {
    return this.http.get<Commande>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
