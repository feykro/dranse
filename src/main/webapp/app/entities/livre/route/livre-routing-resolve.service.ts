import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILivre, Livre } from '../livre.model';
import { LivreService } from '../service/livre.service';

@Injectable({ providedIn: 'root' })
export class LivreRoutingResolveService implements Resolve<ILivre> {
  constructor(protected service: LivreService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILivre> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((livre: HttpResponse<Livre>) => {
          if (livre.body) {
            return of(livre.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Livre());
  }
}
