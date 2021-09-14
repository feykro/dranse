import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILivre, Livre } from '../livre.model';
import { LivreService } from '../service/livre.service';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';

@Component({
  selector: 'jhi-livre-update',
  templateUrl: './livre-update.component.html',
})
export class LivreUpdateComponent implements OnInit {
  isSaving = false;

  categoriesSharedCollection: ICategorie[] = [];

  editForm = this.fb.group({
    id: [],
    titre: [],
    auteur: [],
    prix: [],
    synopsis: [],
    edition: [],
    anneePublication: [],
    editeur: [],
    stock: [],
    urlImage: [],
    categories: [],
  });

  constructor(
    protected livreService: LivreService,
    protected categorieService: CategorieService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ livre }) => {
      this.updateForm(livre);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const livre = this.createFromForm();
    if (livre.id !== undefined) {
      this.subscribeToSaveResponse(this.livreService.update(livre));
    } else {
      this.subscribeToSaveResponse(this.livreService.create(livre));
    }
  }

  trackCategorieById(index: number, item: ICategorie): number {
    return item.id!;
  }

  getSelectedCategorie(option: ICategorie, selectedVals?: ICategorie[]): ICategorie {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILivre>>): void {
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

  protected updateForm(livre: ILivre): void {
    this.editForm.patchValue({
      id: livre.id,
      titre: livre.titre,
      auteur: livre.auteur,
      prix: livre.prix,
      synopsis: livre.synopsis,
      edition: livre.edition,
      anneePublication: livre.anneePublication,
      editeur: livre.editeur,
      stock: livre.stock,
      urlImage: livre.urlImage,
      categories: livre.categories,
    });

    this.categoriesSharedCollection = this.categorieService.addCategorieToCollectionIfMissing(
      this.categoriesSharedCollection,
      ...(livre.categories ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categorieService
      .query()
      .pipe(map((res: HttpResponse<ICategorie[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategorie[]) =>
          this.categorieService.addCategorieToCollectionIfMissing(categories, ...(this.editForm.get('categories')!.value ?? []))
        )
      )
      .subscribe((categories: ICategorie[]) => (this.categoriesSharedCollection = categories));
  }

  protected createFromForm(): ILivre {
    return {
      ...new Livre(),
      id: this.editForm.get(['id'])!.value,
      titre: this.editForm.get(['titre'])!.value,
      auteur: this.editForm.get(['auteur'])!.value,
      prix: this.editForm.get(['prix'])!.value,
      synopsis: this.editForm.get(['synopsis'])!.value,
      edition: this.editForm.get(['edition'])!.value,
      anneePublication: this.editForm.get(['anneePublication'])!.value,
      editeur: this.editForm.get(['editeur'])!.value,
      stock: this.editForm.get(['stock'])!.value,
      urlImage: this.editForm.get(['urlImage'])!.value,
      categories: this.editForm.get(['categories'])!.value,
    };
  }
}
