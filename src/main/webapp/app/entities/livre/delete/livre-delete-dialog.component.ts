import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILivre } from '../livre.model';
import { LivreService } from '../service/livre.service';

@Component({
  templateUrl: './livre-delete-dialog.component.html',
})
export class LivreDeleteDialogComponent {
  livre?: ILivre;

  constructor(protected livreService: LivreService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.livreService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
