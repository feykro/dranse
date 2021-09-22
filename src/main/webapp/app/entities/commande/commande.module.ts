import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CommandeComponent } from './list/commande.component';
import { CommandeDetailComponent } from './detail/commande-detail.component';
import { CommandeUpdateComponent } from './update/commande-update.component';
import { CommandeDeleteDialogComponent } from './delete/commande-delete-dialog.component';
import { CommandeRoutingModule } from './route/commande-routing.module';

@NgModule({
  imports: [SharedModule, CommandeRoutingModule],
  declarations: [CommandeComponent, CommandeDetailComponent, CommandeUpdateComponent, CommandeDeleteDialogComponent],
  entryComponents: [CommandeDeleteDialogComponent],
})
export class CommandeModule {}
