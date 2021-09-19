import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProduitComponent } from './produit/produit.component';
import { DranseRoutingModule } from './dranse-routing.module';
import { AccueilComponent } from './accueil/accueil.component';
import { PanierComponent } from './panier/panier.component';
import { PageRechercheComponent } from './page-recherche/page-recherche.component';

@NgModule({
  declarations: [ProduitComponent, AccueilComponent, PanierComponent, PageRechercheComponent],
  imports: [CommonModule, DranseRoutingModule],
})
export class DranseModule {}
