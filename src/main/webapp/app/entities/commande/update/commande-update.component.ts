import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICommande, Commande } from '../commande.model';
import { CommandeService } from '../service/commande.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';

@Component({
  selector: 'jhi-commande-update',
  templateUrl: './commande-update.component.html',
})
export class CommandeUpdateComponent implements OnInit {
  isSaving = false;

  utilisateursSharedCollection: IUtilisateur[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    paysLivraison: [],
    codePostalLivraison: [],
    villeLivraison: [],
    rueLivraison: [],
    nomLivraison: [],
    paysFacturation: [],
    codePostalFacturation: [],
    villeFacturation: [],
    rueFacturation: [],
    nomFacturation: [],
    payee: [],
    utilisateur: [],
  });

  constructor(
    protected commandeService: CommandeService,
    protected utilisateurService: UtilisateurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commande }) => {
      if (commande.id === undefined) {
        const today = dayjs().startOf('day');
        commande.date = today;
      }

      this.updateForm(commande);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commande = this.createFromForm();
    if (commande.id !== undefined) {
      this.subscribeToSaveResponse(this.commandeService.update(commande));
    } else {
      this.subscribeToSaveResponse(this.commandeService.create(commande));
    }
  }

  trackUtilisateurById(index: number, item: IUtilisateur): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommande>>): void {
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

  protected updateForm(commande: ICommande): void {
    this.editForm.patchValue({
      id: commande.id,
      date: commande.date ? commande.date.format(DATE_TIME_FORMAT) : null,
      paysLivraison: commande.paysLivraison,
      codePostalLivraison: commande.codePostalLivraison,
      villeLivraison: commande.villeLivraison,
      rueLivraison: commande.rueLivraison,
      nomLivraison: commande.nomLivraison,
      paysFacturation: commande.paysFacturation,
      codePostalFacturation: commande.codePostalFacturation,
      villeFacturation: commande.villeFacturation,
      rueFacturation: commande.rueFacturation,
      nomFacturation: commande.nomFacturation,
      payee: commande.payee,
      utilisateur: commande.utilisateur,
    });

    this.utilisateursSharedCollection = this.utilisateurService.addUtilisateurToCollectionIfMissing(
      this.utilisateursSharedCollection,
      commande.utilisateur
    );
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): ICommande {
    return {
      ...new Commande(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      paysLivraison: this.editForm.get(['paysLivraison'])!.value,
      codePostalLivraison: this.editForm.get(['codePostalLivraison'])!.value,
      villeLivraison: this.editForm.get(['villeLivraison'])!.value,
      rueLivraison: this.editForm.get(['rueLivraison'])!.value,
      nomLivraison: this.editForm.get(['nomLivraison'])!.value,
      paysFacturation: this.editForm.get(['paysFacturation'])!.value,
      codePostalFacturation: this.editForm.get(['codePostalFacturation'])!.value,
      villeFacturation: this.editForm.get(['villeFacturation'])!.value,
      rueFacturation: this.editForm.get(['rueFacturation'])!.value,
      nomFacturation: this.editForm.get(['nomFacturation'])!.value,
      payee: this.editForm.get(['payee'])!.value,
      utilisateur: this.editForm.get(['utilisateur'])!.value,
    };
  }
}
