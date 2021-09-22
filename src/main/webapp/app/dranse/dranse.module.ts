import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProduitComponent } from './produit/produit.component';
import { DranseRoutingModule } from './dranse-routing.module';
import { AccueilComponent } from './accueil/accueil.component';
import { PanierComponent } from './panier/panier.component';
import { FormspaiementComponent } from './formspaiement/formspaiement.component';
import { FormsModule } from '@angular/forms';
import { ConfirmationachatComponent } from './confirmationachat/confirmationachat.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { HistoriqueComponent } from './historique/historique.component';
import { PageRechercheComponent} from './page-recherche/page-recherche.component';


@NgModule({
  declarations: [
  ProduitComponent,
   AccueilComponent,
   PanierComponent,
   FormspaiementComponent,
   HistoriqueComponent,
   ConfirmationachatComponent,
   PageRechercheComponent],
  imports: [CommonModule, DranseRoutingModule, FormsModule, FontAwesomeModule],
})
export class DranseModule {}
