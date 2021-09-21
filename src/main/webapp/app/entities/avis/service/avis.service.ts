import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAvis, getAvisIdentifier } from '../avis.model';

export type EntityResponseType = HttpResponse<IAvis>;
export type EntityArrayResponseType = HttpResponse<IAvis[]>;

@Injectable({ providedIn: 'root' })
export class AvisService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/avis');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/avis');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(avis: IAvis): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(avis);
    return this.http
      .post<IAvis>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(avis: IAvis): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(avis);
    return this.http
      .put<IAvis>(`${this.resourceUrl}/${getAvisIdentifier(avis) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(avis: IAvis): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(avis);
    return this.http
      .patch<IAvis>(`${this.resourceUrl}/${getAvisIdentifier(avis) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAvis>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAvis[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAvis[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAvisToCollectionIfMissing(avisCollection: IAvis[], ...avisToCheck: (IAvis | null | undefined)[]): IAvis[] {
    const avis: IAvis[] = avisToCheck.filter(isPresent);
    if (avis.length > 0) {
      const avisCollectionIdentifiers = avisCollection.map(avisItem => getAvisIdentifier(avisItem)!);
      const avisToAdd = avis.filter(avisItem => {
        const avisIdentifier = getAvisIdentifier(avisItem);
        if (avisIdentifier == null || avisCollectionIdentifiers.includes(avisIdentifier)) {
          return false;
        }
        avisCollectionIdentifiers.push(avisIdentifier);
        return true;
      });
      return [...avisToAdd, ...avisCollection];
    }
    return avisCollection;
  }

  protected convertDateFromClient(avis: IAvis): IAvis {
    return Object.assign({}, avis, {
      datePublication: avis.datePublication?.isValid() ? avis.datePublication.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.datePublication = res.body.datePublication ? dayjs(res.body.datePublication) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((avis: IAvis) => {
        avis.datePublication = avis.datePublication ? dayjs(avis.datePublication) : undefined;
      });
    }
    return res;
  }
}
