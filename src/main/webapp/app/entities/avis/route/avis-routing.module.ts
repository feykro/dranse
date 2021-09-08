import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AvisComponent } from '../list/avis.component';
import { AvisDetailComponent } from '../detail/avis-detail.component';
import { AvisUpdateComponent } from '../update/avis-update.component';
import { AvisRoutingResolveService } from './avis-routing-resolve.service';

const avisRoute: Routes = [
  {
    path: '',
    component: AvisComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AvisDetailComponent,
    resolve: {
      avis: AvisRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AvisUpdateComponent,
    resolve: {
      avis: AvisRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AvisUpdateComponent,
    resolve: {
      avis: AvisRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(avisRoute)],
  exports: [RouterModule],
})
export class AvisRoutingModule {}
