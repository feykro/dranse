import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategorie } from '../categorie.model';
import { CategorieService } from '../service/categorie.service';

@Component({
  templateUrl: './categorie-delete-dialog.component.html',
})
export class CategorieDeleteDialogComponent {
  categorie?: ICategorie;

  constructor(protected categorieService: CategorieService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.categorieService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
