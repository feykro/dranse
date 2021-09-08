import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommandeComponent } from '../list/commande.component';
import { CommandeDetailComponent } from '../detail/commande-detail.component';
import { CommandeUpdateComponent } from '../update/commande-update.component';
import { CommandeRoutingResolveService } from './commande-routing-resolve.service';

const commandeRoute: Routes = [
  {
    path: '',
    component: CommandeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommandeDetailComponent,
    resolve: {
      commande: CommandeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommandeUpdateComponent,
    resolve: {
      commande: CommandeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommandeUpdateComponent,
    resolve: {
      commande: CommandeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(commandeRoute)],
  exports: [RouterModule],
})
export class CommandeRoutingModule {}
