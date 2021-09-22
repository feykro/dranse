import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CategorieComponent } from '../list/categorie.component';
import { CategorieDetailComponent } from '../detail/categorie-detail.component';
import { CategorieUpdateComponent } from '../update/categorie-update.component';
import { CategorieRoutingResolveService } from './categorie-routing-resolve.service';

const categorieRoute: Routes = [
  {
    path: '',
    component: CategorieComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CategorieDetailComponent,
    resolve: {
      categorie: CategorieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CategorieUpdateComponent,
    resolve: {
      categorie: CategorieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CategorieUpdateComponent,
    resolve: {
      categorie: CategorieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(categorieRoute)],
  exports: [RouterModule],
})
export class CategorieRoutingModule {}
