import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAvis, Avis } from '../avis.model';
import { AvisService } from '../service/avis.service';
import { ILivre } from 'app/entities/livre/livre.model';
import { LivreService } from 'app/entities/livre/service/livre.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';

@Component({
  selector: 'jhi-avis-update',
  templateUrl: './avis-update.component.html',
})
export class AvisUpdateComponent implements OnInit {
  isSaving = false;

  livresSharedCollection: ILivre[] = [];
  utilisateursSharedCollection: IUtilisateur[] = [];

  editForm = this.fb.group({
    id: [],
    note: [null, [Validators.min(0), Validators.max(10)]],
    commentaire: [],
    datePublication: [],
    affiche: [],
    livre: [],
    utilisateur: [],
  });

  constructor(
    protected avisService: AvisService,
    protected livreService: LivreService,
    protected utilisateurService: UtilisateurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ avis }) => {
      if (avis.id === undefined) {
        const today = dayjs().startOf('day');
        avis.datePublication = today;
      }

      this.updateForm(avis);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const avis = this.createFromForm();
    if (avis.id !== undefined) {
      this.subscribeToSaveResponse(this.avisService.update(avis));
    } else {
      this.subscribeToSaveResponse(this.avisService.create(avis));
    }
  }

  trackLivreById(index: number, item: ILivre): number {
    return item.id!;
  }

  trackUtilisateurById(index: number, item: IUtilisateur): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAvis>>): void {
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

  protected updateForm(avis: IAvis): void {
    this.editForm.patchValue({
      id: avis.id,
      note: avis.note,
      commentaire: avis.commentaire,
      datePublication: avis.datePublication ? avis.datePublication.format(DATE_TIME_FORMAT) : null,
      affiche: avis.affiche,
      livre: avis.livre,
      utilisateur: avis.utilisateur,
    });

    this.livresSharedCollection = this.livreService.addLivreToCollectionIfMissing(this.livresSharedCollection, avis.livre);
    this.utilisateursSharedCollection = this.utilisateurService.addUtilisateurToCollectionIfMissing(
      this.utilisateursSharedCollection,
      avis.utilisateur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.livreService
      .query()
      .pipe(map((res: HttpResponse<ILivre[]>) => res.body ?? []))
      .pipe(map((livres: ILivre[]) => this.livreService.addLivreToCollectionIfMissing(livres, this.editForm.get('livre')!.value)))
      .subscribe((livres: ILivre[]) => (this.livresSharedCollection = livres));

    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing(utilisateurs, this.editForm.get('utilisateur')!.value)
        )
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursSharedCollection = utilisateurs));
  }

  protected createFromForm(): IAvis {
    return {
      ...new Avis(),
      id: this.editForm.get(['id'])!.value,
      note: this.editForm.get(['note'])!.value,
      commentaire: this.editForm.get(['commentaire'])!.value,
      datePublication: this.editForm.get(['datePublication'])!.value
        ? dayjs(this.editForm.get(['datePublication'])!.value, DATE_TIME_FORMAT)
        : undefined,
      affiche: this.editForm.get(['affiche'])!.value,
      livre: this.editForm.get(['livre'])!.value,
      utilisateur: this.editForm.get(['utilisateur'])!.value,
    };
  }
}
