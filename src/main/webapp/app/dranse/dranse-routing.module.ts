import { AccueilComponent } from './accueil/accueil.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ProduitComponent } from './produit/produit.component';
import { PanierComponent } from './panier/panier.component';
import { FormspaiementComponent } from './formspaiement/formspaiement.component';
import { HistoriqueComponent } from './historique/historique.component';
import { UserRouteAccessService } from '../core/auth/user-route-access.service';
import { ConfirmationachatComponent } from './confirmationachat/confirmationachat.component';

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
      path: 'historique',
      component: HistoriqueComponent,
    },
  {
    path: 'verification',
    component: FormspaiementComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'confirmation',
    component: ConfirmationachatComponent,
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
