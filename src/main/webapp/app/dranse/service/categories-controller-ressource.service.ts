import { Observable } from 'rxjs';
import { ICategorie } from './../../entities/categorie/categorie.model';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

export type EntityResponseType = HttpResponse<ICategorie>;
export type EntityArrayResponseType = HttpResponse<ICategorie[]>;

@Injectable({
  providedIn: 'root',
})
export class CategoriesControllerRessourceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('/api/v1/categorie');

  constructor(private http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getPopularCategories(): Observable<EntityArrayResponseType> {
    return this.http.get<ICategorie[]>(`${this.resourceUrl}/most-popular`, { observe: 'response' });
  }
}
