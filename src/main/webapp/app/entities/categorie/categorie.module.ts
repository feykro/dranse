import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CategorieComponent } from './list/categorie.component';
import { CategorieDetailComponent } from './detail/categorie-detail.component';
import { CategorieUpdateComponent } from './update/categorie-update.component';
import { CategorieDeleteDialogComponent } from './delete/categorie-delete-dialog.component';
import { CategorieRoutingModule } from './route/categorie-routing.module';

@NgModule({
  imports: [SharedModule, CategorieRoutingModule],
  declarations: [CategorieComponent, CategorieDetailComponent, CategorieUpdateComponent, CategorieDeleteDialogComponent],
  entryComponents: [CategorieDeleteDialogComponent],
})
export class CategorieModule {}
