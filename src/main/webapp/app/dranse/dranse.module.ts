import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProduitComponent } from './produit/produit.component';
import { DranseRoutingModule } from './dranse-routing.module';
import { AccueilComponent } from './accueil/accueil.component';
import { BookCardComponent } from './components/book-card/book-card.component';

@NgModule({
  declarations: [ProduitComponent, AccueilComponent, BookCardComponent],
  imports: [CommonModule, DranseRoutingModule],
})
export class DranseModule {}
