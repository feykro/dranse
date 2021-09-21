import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UtilisateurComponent } from './list/utilisateur.component';
import { UtilisateurDetailComponent } from './detail/utilisateur-detail.component';
import { UtilisateurUpdateComponent } from './update/utilisateur-update.component';
import { UtilisateurDeleteDialogComponent } from './delete/utilisateur-delete-dialog.component';
import { UtilisateurRoutingModule } from './route/utilisateur-routing.module';

@NgModule({
  imports: [SharedModule, UtilisateurRoutingModule],
  declarations: [UtilisateurComponent, UtilisateurDetailComponent, UtilisateurUpdateComponent, UtilisateurDeleteDialogComponent],
  entryComponents: [UtilisateurDeleteDialogComponent],
})
export class UtilisateurModule {}
