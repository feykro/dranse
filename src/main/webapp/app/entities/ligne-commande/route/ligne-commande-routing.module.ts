import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LigneCommandeComponent } from '../list/ligne-commande.component';
import { LigneCommandeDetailComponent } from '../detail/ligne-commande-detail.component';
import { LigneCommandeUpdateComponent } from '../update/ligne-commande-update.component';
import { LigneCommandeRoutingResolveService } from './ligne-commande-routing-resolve.service';

const ligneCommandeRoute: Routes = [
  {
    path: '',
    component: LigneCommandeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LigneCommandeDetailComponent,
    resolve: {
      ligneCommande: LigneCommandeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LigneCommandeUpdateComponent,
    resolve: {
      ligneCommande: LigneCommandeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LigneCommandeUpdateComponent,
    resolve: {
      ligneCommande: LigneCommandeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ligneCommandeRoute)],
  exports: [RouterModule],
})
export class LigneCommandeRoutingModule {}
