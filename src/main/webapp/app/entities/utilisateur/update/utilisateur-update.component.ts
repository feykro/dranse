import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IUtilisateur, Utilisateur } from '../utilisateur.model';
import { UtilisateurService } from '../service/utilisateur.service';

@Component({
  selector: 'jhi-utilisateur-update',
  templateUrl: './utilisateur-update.component.html',
})
export class UtilisateurUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    mail: [],
    motDePasse: [],
    nom: [],
    prenom: [],
    adrRue: [],
    adrCodePostal: [],
    adrPays: [],
    adrVille: [],
    telephone: [],
    numCB: [],
  });

  constructor(protected utilisateurService: UtilisateurService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utilisateur }) => {
      this.updateForm(utilisateur);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const utilisateur = this.createFromForm();
    if (utilisateur.id !== undefined) {
      this.subscribeToSaveResponse(this.utilisateurService.update(utilisateur));
    } else {
      this.subscribeToSaveResponse(this.utilisateurService.create(utilisateur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUtilisateur>>): void {
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

  protected updateForm(utilisateur: IUtilisateur): void {
    this.editForm.patchValue({
      id: utilisateur.id,
      mail: utilisateur.mail,
      motDePasse: utilisateur.motDePasse,
      nom: utilisateur.nom,
      prenom: utilisateur.prenom,
      adrRue: utilisateur.adrRue,
      adrCodePostal: utilisateur.adrCodePostal,
      adrPays: utilisateur.adrPays,
      adrVille: utilisateur.adrVille,
      telephone: utilisateur.telephone,
      numCB: utilisateur.numCB,
    });
  }

  protected createFromForm(): IUtilisateur {
    return {
      ...new Utilisateur(),
      id: this.editForm.get(['id'])!.value,
      mail: this.editForm.get(['mail'])!.value,
      motDePasse: this.editForm.get(['motDePasse'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      adrRue: this.editForm.get(['adrRue'])!.value,
      adrCodePostal: this.editForm.get(['adrCodePostal'])!.value,
      adrPays: this.editForm.get(['adrPays'])!.value,
      adrVille: this.editForm.get(['adrVille'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      numCB: this.editForm.get(['numCB'])!.value,
    };
  }
}
