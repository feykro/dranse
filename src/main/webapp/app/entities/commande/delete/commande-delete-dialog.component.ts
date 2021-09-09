import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommande } from '../commande.model';
import { CommandeService } from '../service/commande.service';

@Component({
  templateUrl: './commande-delete-dialog.component.html',
})
export class CommandeDeleteDialogComponent {
  commande?: ICommande;

  constructor(protected commandeService: CommandeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.commandeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
