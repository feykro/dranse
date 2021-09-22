import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommande } from '../commande.model';

@Component({
  selector: 'jhi-commande-detail',
  templateUrl: './commande-detail.component.html',
})
export class CommandeDetailComponent implements OnInit {
  commande: ICommande | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commande }) => {
      this.commande = commande;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
