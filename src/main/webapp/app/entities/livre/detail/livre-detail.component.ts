import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILivre } from '../livre.model';

@Component({
  selector: 'jhi-livre-detail',
  templateUrl: './livre-detail.component.html',
})
export class LivreDetailComponent implements OnInit {
  livre: ILivre | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ livre }) => {
      this.livre = livre;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
