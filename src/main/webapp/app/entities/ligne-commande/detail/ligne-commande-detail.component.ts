import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILigneCommande } from '../ligne-commande.model';

@Component({
  selector: 'jhi-ligne-commande-detail',
  templateUrl: './ligne-commande-detail.component.html',
})
export class LigneCommandeDetailComponent implements OnInit {
  ligneCommande: ILigneCommande | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ligneCommande }) => {
      this.ligneCommande = ligneCommande;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
