import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LigneCommandeComponent } from './list/ligne-commande.component';
import { LigneCommandeDetailComponent } from './detail/ligne-commande-detail.component';
import { LigneCommandeUpdateComponent } from './update/ligne-commande-update.component';
import { LigneCommandeDeleteDialogComponent } from './delete/ligne-commande-delete-dialog.component';
import { LigneCommandeRoutingModule } from './route/ligne-commande-routing.module';

@NgModule({
  imports: [SharedModule, LigneCommandeRoutingModule],
  declarations: [LigneCommandeComponent, LigneCommandeDetailComponent, LigneCommandeUpdateComponent, LigneCommandeDeleteDialogComponent],
  entryComponents: [LigneCommandeDeleteDialogComponent],
})
export class LigneCommandeModule {}
