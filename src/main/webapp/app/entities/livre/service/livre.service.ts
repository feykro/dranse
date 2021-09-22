import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ILivre, getLivreIdentifier } from '../livre.model';

export type EntityResponseType = HttpResponse<ILivre>;
export type EntityArrayResponseType = HttpResponse<ILivre[]>;

@Injectable({ providedIn: 'root' })
export class LivreService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/livres');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/livres');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(livre: ILivre): Observable<EntityResponseType> {
    return this.http.post<ILivre>(this.resourceUrl, livre, { observe: 'response' });
  }

  update(livre: ILivre): Observable<EntityResponseType> {
    return this.http.put<ILivre>(`${this.resourceUrl}/${getLivreIdentifier(livre) as number}`, livre, { observe: 'response' });
  }

  partialUpdate(livre: ILivre): Observable<EntityResponseType> {
    return this.http.patch<ILivre>(`${this.resourceUrl}/${getLivreIdentifier(livre) as number}`, livre, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILivre>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILivre[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILivre[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addLivreToCollectionIfMissing(livreCollection: ILivre[], ...livresToCheck: (ILivre | null | undefined)[]): ILivre[] {
    const livres: ILivre[] = livresToCheck.filter(isPresent);
    if (livres.length > 0) {
      const livreCollectionIdentifiers = livreCollection.map(livreItem => getLivreIdentifier(livreItem)!);
      const livresToAdd = livres.filter(livreItem => {
        const livreIdentifier = getLivreIdentifier(livreItem);
        if (livreIdentifier == null || livreCollectionIdentifiers.includes(livreIdentifier)) {
          return false;
        }
        livreCollectionIdentifiers.push(livreIdentifier);
        return true;
      });
      return [...livresToAdd, ...livreCollection];
    }
    return livreCollection;
  }
}
