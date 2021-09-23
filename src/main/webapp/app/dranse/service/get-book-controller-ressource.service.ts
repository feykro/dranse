import { Observable } from 'rxjs';
import { ApplicationConfigService } from './../../core/config/application-config.service';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ILivre } from 'app/entities/livre/livre.model';

export type EntityResponseType = HttpResponse<ILivre>;
export type EntityArrayResponseType = HttpResponse<ILivre[]>;

@Injectable({
  providedIn: 'root',
})
export class GetBookControllerRessourceService {
  protected resourceUrl = this.appConfigService.getEndpointFor('api/livre-controller');

  constructor(private http: HttpClient, protected appConfigService: ApplicationConfigService) {}

  getPageParCategorie(categorie: string, numPage: number, pageSize: number): Observable<EntityArrayResponseType> {
    return this.http.get<ILivre[]>(`${this.resourceUrl}/recherche-par-categorie/${categorie}?page=${numPage}&size=${pageSize})`, {
      observe: 'response',
    });
  }

  getPageRecherche(recherche: string, numPage: number, pageSize: number): Observable<EntityArrayResponseType> {
    return this.http.get<ILivre[]>(`${this.resourceUrl}/recherche/${recherche}?page=${numPage}&size=${pageSize})`, {
      observe: 'response',
    });
  }
}
