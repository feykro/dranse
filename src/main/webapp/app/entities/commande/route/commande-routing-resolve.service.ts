import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommande, Commande } from '../commande.model';
import { CommandeService } from '../service/commande.service';

@Injectable({ providedIn: 'root' })
export class CommandeRoutingResolveService implements Resolve<ICommande> {
  constructor(protected service: CommandeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICommande> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((commande: HttpResponse<Commande>) => {
          if (commande.body) {
            return of(commande.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Commande());
  }
}
