import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICategorie } from '../categorie.model';

@Component({
  selector: 'jhi-categorie-detail',
  templateUrl: './categorie-detail.component.html',
})
export class CategorieDetailComponent implements OnInit {
  categorie: ICategorie | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categorie }) => {
      this.categorie = categorie;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
