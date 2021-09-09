import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LivreComponent } from './list/livre.component';
import { LivreDetailComponent } from './detail/livre-detail.component';
import { LivreUpdateComponent } from './update/livre-update.component';
import { LivreDeleteDialogComponent } from './delete/livre-delete-dialog.component';
import { LivreRoutingModule } from './route/livre-routing.module';

@NgModule({
  imports: [SharedModule, LivreRoutingModule],
  declarations: [LivreComponent, LivreDetailComponent, LivreUpdateComponent, LivreDeleteDialogComponent],
  entryComponents: [LivreDeleteDialogComponent],
})
export class LivreModule {}
