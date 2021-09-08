import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AvisComponent } from './list/avis.component';
import { AvisDetailComponent } from './detail/avis-detail.component';
import { AvisUpdateComponent } from './update/avis-update.component';
import { AvisDeleteDialogComponent } from './delete/avis-delete-dialog.component';
import { AvisRoutingModule } from './route/avis-routing.module';

@NgModule({
  imports: [SharedModule, AvisRoutingModule],
  declarations: [AvisComponent, AvisDetailComponent, AvisUpdateComponent, AvisDeleteDialogComponent],
  entryComponents: [AvisDeleteDialogComponent],
})
export class AvisModule {}
