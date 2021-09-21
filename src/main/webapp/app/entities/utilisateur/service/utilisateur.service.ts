import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IUtilisateur, getUtilisateurIdentifier } from '../utilisateur.model';

export type EntityResponseType = HttpResponse<IUtilisateur>;
export type EntityArrayResponseType = HttpResponse<IUtilisateur[]>;

@Injectable({ providedIn: 'root' })
export class UtilisateurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/utilisateurs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/utilisateurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(utilisateur: IUtilisateur): Observable<EntityResponseType> {
    return this.http.post<IUtilisateur>(this.resourceUrl, utilisateur, { observe: 'response' });
  }

  update(utilisateur: IUtilisateur): Observable<EntityResponseType> {
    return this.http.put<IUtilisateur>(`${this.resourceUrl}/${getUtilisateurIdentifier(utilisateur) as number}`, utilisateur, {
      observe: 'response',
    });
  }

  partialUpdate(utilisateur: IUtilisateur): Observable<EntityResponseType> {
    return this.http.patch<IUtilisateur>(`${this.resourceUrl}/${getUtilisateurIdentifier(utilisateur) as number}`, utilisateur, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUtilisateur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUtilisateur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUtilisateur[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addUtilisateurToCollectionIfMissing(
    utilisateurCollection: IUtilisateur[],
    ...utilisateursToCheck: (IUtilisateur | null | undefined)[]
  ): IUtilisateur[] {
    const utilisateurs: IUtilisateur[] = utilisateursToCheck.filter(isPresent);
    if (utilisateurs.length > 0) {
      const utilisateurCollectionIdentifiers = utilisateurCollection.map(utilisateurItem => getUtilisateurIdentifier(utilisateurItem)!);
      const utilisateursToAdd = utilisateurs.filter(utilisateurItem => {
        const utilisateurIdentifier = getUtilisateurIdentifier(utilisateurItem);
        if (utilisateurIdentifier == null || utilisateurCollectionIdentifiers.includes(utilisateurIdentifier)) {
          return false;
        }
        utilisateurCollectionIdentifiers.push(utilisateurIdentifier);
        return true;
      });
      return [...utilisateursToAdd, ...utilisateurCollection];
    }
    return utilisateurCollection;
  }
}
