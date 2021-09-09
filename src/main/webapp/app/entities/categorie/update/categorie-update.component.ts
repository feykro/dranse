import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICategorie, Categorie } from '../categorie.model';
import { CategorieService } from '../service/categorie.service';
import { ILivre } from 'app/entities/livre/livre.model';
import { LivreService } from 'app/entities/livre/service/livre.service';

@Component({
  selector: 'jhi-categorie-update',
  templateUrl: './categorie-update.component.html',
})
export class CategorieUpdateComponent implements OnInit {
  isSaving = false;

  livresSharedCollection: ILivre[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    description: [],
    livres: [],
  });

  constructor(
    protected categorieService: CategorieService,
    protected livreService: LivreService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categorie }) => {
      this.updateForm(categorie);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const categorie = this.createFromForm();
    if (categorie.id !== undefined) {
      this.subscribeToSaveResponse(this.categorieService.update(categorie));
    } else {
      this.subscribeToSaveResponse(this.categorieService.create(categorie));
    }
  }

  trackLivreById(index: number, item: ILivre): number {
    return item.id!;
  }

  getSelectedLivre(option: ILivre, selectedVals?: ILivre[]): ILivre {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategorie>>): void {
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

  protected updateForm(categorie: ICategorie): void {
    this.editForm.patchValue({
      id: categorie.id,
      nom: categorie.nom,
      description: categorie.description,
      livres: categorie.livres,
    });

    this.livresSharedCollection = this.livreService.addLivreToCollectionIfMissing(this.livresSharedCollection, ...(categorie.livres ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.livreService
      .query()
      .pipe(map((res: HttpResponse<ILivre[]>) => res.body ?? []))
      .pipe(
        map((livres: ILivre[]) => this.livreService.addLivreToCollectionIfMissing(livres, ...(this.editForm.get('livres')!.value ?? [])))
      )
      .subscribe((livres: ILivre[]) => (this.livresSharedCollection = livres));
  }

  protected createFromForm(): ICategorie {
    return {
      ...new Categorie(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      description: this.editForm.get(['description'])!.value,
      livres: this.editForm.get(['livres'])!.value,
    };
  }
}
