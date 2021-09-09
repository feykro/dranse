import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAvis, Avis } from '../avis.model';
import { AvisService } from '../service/avis.service';

@Injectable({ providedIn: 'root' })
export class AvisRoutingResolveService implements Resolve<IAvis> {
  constructor(protected service: AvisService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAvis> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((avis: HttpResponse<Avis>) => {
          if (avis.body) {
            return of(avis.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Avis());
  }
}
