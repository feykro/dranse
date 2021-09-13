import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ProduitComponent } from "./produit/produit.component";

const dranseRoute: Routes = [
  {
    path: 'produit',
    component: ProduitComponent,
    //resolve: {
    //  livre: LivreRoutingResolveService,
    //},
    //canActivate: [UserRouteAccessService],
  }
];

@NgModule({
  imports: [RouterModule.forChild(dranseRoute)],
  exports: [RouterModule],
})
export class DranseRoutingModule {}
