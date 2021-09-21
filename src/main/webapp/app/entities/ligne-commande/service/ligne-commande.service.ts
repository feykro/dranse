import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ILigneCommande, getLigneCommandeIdentifier } from '../ligne-commande.model';

export type EntityResponseType = HttpResponse<ILigneCommande>;
export type EntityArrayResponseType = HttpResponse<ILigneCommande[]>;

@Injectable({ providedIn: 'root' })
export class LigneCommandeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ligne-commandes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/ligne-commandes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ligneCommande: ILigneCommande): Observable<EntityResponseType> {
    return this.http.post<ILigneCommande>(this.resourceUrl, ligneCommande, { observe: 'response' });
  }

  update(ligneCommande: ILigneCommande): Observable<EntityResponseType> {
    return this.http.put<ILigneCommande>(`${this.resourceUrl}/${getLigneCommandeIdentifier(ligneCommande) as number}`, ligneCommande, {
      observe: 'response',
    });
  }

  partialUpdate(ligneCommande: ILigneCommande): Observable<EntityResponseType> {
    return this.http.patch<ILigneCommande>(`${this.resourceUrl}/${getLigneCommandeIdentifier(ligneCommande) as number}`, ligneCommande, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILigneCommande>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILigneCommande[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILigneCommande[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addLigneCommandeToCollectionIfMissing(
    ligneCommandeCollection: ILigneCommande[],
    ...ligneCommandesToCheck: (ILigneCommande | null | undefined)[]
  ): ILigneCommande[] {
    const ligneCommandes: ILigneCommande[] = ligneCommandesToCheck.filter(isPresent);
    if (ligneCommandes.length > 0) {
      const ligneCommandeCollectionIdentifiers = ligneCommandeCollection.map(
        ligneCommandeItem => getLigneCommandeIdentifier(ligneCommandeItem)!
      );
      const ligneCommandesToAdd = ligneCommandes.filter(ligneCommandeItem => {
        const ligneCommandeIdentifier = getLigneCommandeIdentifier(ligneCommandeItem);
        if (ligneCommandeIdentifier == null || ligneCommandeCollectionIdentifiers.includes(ligneCommandeIdentifier)) {
          return false;
        }
        ligneCommandeCollectionIdentifiers.push(ligneCommandeIdentifier);
        return true;
      });
      return [...ligneCommandesToAdd, ...ligneCommandeCollection];
    }
    return ligneCommandeCollection;
  }
}
