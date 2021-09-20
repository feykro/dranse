import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { Observable } from 'rxjs';

export type EntityResponseType = HttpResponse<IUtilisateur>;
export type EntityArrayResponseType = HttpResponse<IUtilisateur[]>;

@Injectable({
  providedIn: 'root',
})
export class UtilisateurControllerRessourceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('/api/utilisateur-controller');

  constructor(private http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  utilisateurCourrant(): Observable<EntityResponseType> {
    return this.http.get<IUtilisateur>(`${this.resourceUrl}/utilisateur-courant`, { observe: 'response' });
  }
}
