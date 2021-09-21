import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUtilisateur } from '../utilisateur.model';
import { UtilisateurService } from '../service/utilisateur.service';

@Component({
  templateUrl: './utilisateur-delete-dialog.component.html',
})
export class UtilisateurDeleteDialogComponent {
  utilisateur?: IUtilisateur;

  constructor(protected utilisateurService: UtilisateurService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.utilisateurService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
