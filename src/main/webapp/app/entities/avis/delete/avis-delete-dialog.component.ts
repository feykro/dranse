import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAvis } from '../avis.model';
import { AvisService } from '../service/avis.service';

@Component({
  templateUrl: './avis-delete-dialog.component.html',
})
export class AvisDeleteDialogComponent {
  avis?: IAvis;

  constructor(protected avisService: AvisService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.avisService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
