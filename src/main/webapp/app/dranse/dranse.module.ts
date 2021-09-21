import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProduitComponent } from './produit/produit.component';
import { DranseRoutingModule } from './dranse-routing.module';
import { AccueilComponent } from './accueil/accueil.component';
import { PanierComponent } from './panier/panier.component';
import { FormspaiementComponent } from './formspaiement/formspaiement.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [ProduitComponent, AccueilComponent, PanierComponent, FormspaiementComponent],
  imports: [CommonModule, DranseRoutingModule, FormsModule],
})
export class DranseModule {}
