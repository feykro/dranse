import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILigneCommande } from '../ligne-commande.model';
import { LigneCommandeService } from '../service/ligne-commande.service';

@Component({
  templateUrl: './ligne-commande-delete-dialog.component.html',
})
export class LigneCommandeDeleteDialogComponent {
  ligneCommande?: ILigneCommande;

  constructor(protected ligneCommandeService: LigneCommandeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ligneCommandeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
