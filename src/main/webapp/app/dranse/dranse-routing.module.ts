import { AccueilComponent } from './accueil/accueil.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ProduitComponent } from './produit/produit.component';
import { PanierComponent } from './panier/panier.component';
import { FormspaiementComponent } from './formspaiement/formspaiement.component';
import { UserRouteAccessService } from '../core/auth/user-route-access.service';

const dranseRoute: Routes = [
  {
    path: 'produit/:livreId',
    component: ProduitComponent,
    //resolve: {
    //  livre: LivreRoutingResolveService,
    //},
    //canActivate: [UserRouteAccessService],
  },
  {
    path: 'panier',
    component: PanierComponent,
  },
  {
    path: 'verification',
    component: FormspaiementComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: '',
    component: AccueilComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(dranseRoute)],
  exports: [RouterModule],
})
export class DranseRoutingModule {}
