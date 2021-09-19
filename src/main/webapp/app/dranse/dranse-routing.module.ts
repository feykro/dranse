import { PageRechercheComponent } from './page-recherche/page-recherche.component';
import { AccueilComponent } from './accueil/accueil.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ProduitComponent } from './produit/produit.component';
import { PanierComponent } from './panier/panier.component';

const dranseRoute: Routes = [
  {
    path: 'produit/:livreId',
    component: ProduitComponent,
  },
  {
    path: 'panier',
    component: PanierComponent,
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
