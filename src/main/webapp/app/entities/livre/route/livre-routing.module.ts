import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LivreComponent } from '../list/livre.component';
import { LivreDetailComponent } from '../detail/livre-detail.component';
import { LivreUpdateComponent } from '../update/livre-update.component';
import { LivreRoutingResolveService } from './livre-routing-resolve.service';

const livreRoute: Routes = [
  {
    path: '',
    component: LivreComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LivreDetailComponent,
    resolve: {
      livre: LivreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LivreUpdateComponent,
    resolve: {
      livre: LivreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LivreUpdateComponent,
    resolve: {
      livre: LivreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(livreRoute)],
  exports: [RouterModule],
})
export class LivreRoutingModule {}
