import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILigneCommande, LigneCommande } from '../ligne-commande.model';
import { LigneCommandeService } from '../service/ligne-commande.service';

@Injectable({ providedIn: 'root' })
export class LigneCommandeRoutingResolveService implements Resolve<ILigneCommande> {
  constructor(protected service: LigneCommandeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILigneCommande> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ligneCommande: HttpResponse<LigneCommande>) => {
          if (ligneCommande.body) {
            return of(ligneCommande.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LigneCommande());
  }
}
