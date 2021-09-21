import { PageRechercheComponent } from './page-recherche/page-recherche.component';
import { AccueilComponent } from './accueil/accueil.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ProduitComponent } from './produit/produit.component';
import { PanierComponent } from './panier/panier.component';
import { FormspaiementComponent } from './formspaiement/formspaiement.component';

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
  },
  {
    path: '',
    component: AccueilComponent,
  },
  {
    path: 'recherche',
    component: PageRechercheComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(dranseRoute)],
  exports: [RouterModule],
})
export class DranseRoutingModule {}
