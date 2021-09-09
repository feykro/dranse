import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UtilisateurComponent } from '../list/utilisateur.component';
import { UtilisateurDetailComponent } from '../detail/utilisateur-detail.component';
import { UtilisateurUpdateComponent } from '../update/utilisateur-update.component';
import { UtilisateurRoutingResolveService } from './utilisateur-routing-resolve.service';

const utilisateurRoute: Routes = [
  {
    path: '',
    component: UtilisateurComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UtilisateurDetailComponent,
    resolve: {
      utilisateur: UtilisateurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UtilisateurUpdateComponent,
    resolve: {
      utilisateur: UtilisateurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UtilisateurUpdateComponent,
    resolve: {
      utilisateur: UtilisateurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(utilisateurRoute)],
  exports: [RouterModule],
})
export class UtilisateurRoutingModule {}
