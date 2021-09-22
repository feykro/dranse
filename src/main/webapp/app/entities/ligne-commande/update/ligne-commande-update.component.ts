import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILigneCommande, LigneCommande } from '../ligne-commande.model';
import { LigneCommandeService } from '../service/ligne-commande.service';
import { ICommande } from 'app/entities/commande/commande.model';
import { CommandeService } from 'app/entities/commande/service/commande.service';
import { ILivre } from 'app/entities/livre/livre.model';
import { LivreService } from 'app/entities/livre/service/livre.service';

@Component({
  selector: 'jhi-ligne-commande-update',
  templateUrl: './ligne-commande-update.component.html',
})
export class LigneCommandeUpdateComponent implements OnInit {
  isSaving = false;

  commandesSharedCollection: ICommande[] = [];
  livresSharedCollection: ILivre[] = [];

  editForm = this.fb.group({
    id: [],
    quantite: [],
    prixPaye: [],
    commande: [],
    livre: [],
  });

  constructor(
    protected ligneCommandeService: LigneCommandeService,
    protected commandeService: CommandeService,
    protected livreService: LivreService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ligneCommande }) => {
      this.updateForm(ligneCommande);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ligneCommande = this.createFromForm();
    if (ligneCommande.id !== undefined) {
      this.subscribeToSaveResponse(this.ligneCommandeService.update(ligneCommande));
    } else {
      this.subscribeToSaveResponse(this.ligneCommandeService.create(ligneCommande));
    }
  }

  trackCommandeById(index: number, item: ICommande): number {
    return item.id!;
  }

  trackLivreById(index: number, item: ILivre): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILigneCommande>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(ligneCommande: ILigneCommande): void {
    this.editForm.patchValue({
      id: ligneCommande.id,
      quantite: ligneCommande.quantite,
      prixPaye: ligneCommande.prixPaye,
      commande: ligneCommande.commande,
      livre: ligneCommande.livre,
    });

    this.commandesSharedCollection = this.commandeService.addCommandeToCollectionIfMissing(
      this.commandesSharedCollection,
      ligneCommande.commande
    );
    this.livresSharedCollection = this.livreService.addLivreToCollectionIfMissing(this.livresSharedCollection, ligneCommande.livre);
  }

  protected loadRelationshipsOptions(): void {
    this.commandeService
      .query()
      .pipe(map((res: HttpResponse<ICommande[]>) => res.body ?? []))
      .pipe(
        map((commandes: ICommande[]) =>
          this.commandeService.addCommandeToCollectionIfMissing(commandes, this.editForm.get('commande')!.value)
        )
      )
      .subscribe((commandes: ICommande[]) => (this.commandesSharedCollection = commandes));

    this.livreService
      .query()
      .pipe(map((res: HttpResponse<ILivre[]>) => res.body ?? []))
      .pipe(map((livres: ILivre[]) => this.livreService.addLivreToCollectionIfMissing(livres, this.editForm.get('livre')!.value)))
      .subscribe((livres: ILivre[]) => (this.livresSharedCollection = livres));
  }

  protected createFromForm(): ILigneCommande {
    return {
      ...new LigneCommande(),
      id: this.editForm.get(['id'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      prixPaye: this.editForm.get(['prixPaye'])!.value,
      commande: this.editForm.get(['commande'])!.value,
      livre: this.editForm.get(['livre'])!.value,
    };
  }
}
