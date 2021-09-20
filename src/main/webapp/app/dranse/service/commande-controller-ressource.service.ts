import { ILigneCommande } from './../../entities/ligne-commande/ligne-commande.model';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ICommande } from 'app/entities/commande/commande.model';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

export type EntityResponseType = HttpResponse<ICommande>;
export type EntityArrayResponseType = HttpResponse<ICommande[]>;

@Injectable({
  providedIn: 'root',
})
export class CommandeControllerRessourceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/v1/commande');

  constructor(private http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  creationCommande(ligneCommande: ILigneCommande): Observable<EntityResponseType> {
    return this.http.post<ICommande>(`${this.resourceUrl}`, ligneCommande, { observe: 'response' });
  }

  ajoutLigneCommande(ligneCommande: ILigneCommande, id: number): Observable<EntityResponseType> {
    return this.http.put<ICommande>(`${this.resourceUrl}/ajout/${id}`, ligneCommande, { observe: 'response' });
  }

  getCommande(id: number): Observable<EntityResponseType> {
    return this.http.get<ICommande>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  passerCommande(id: number, commande: ICommande): Observable<HttpResponse<boolean>> {
    return this.http.put<boolean>(`${this.resourceUrl}/commander/${id}`, commande, { observe: 'response' });
  }
}
