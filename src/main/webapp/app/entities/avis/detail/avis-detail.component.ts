import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAvis } from '../avis.model';

@Component({
  selector: 'jhi-avis-detail',
  templateUrl: './avis-detail.component.html',
})
export class AvisDetailComponent implements OnInit {
  avis: IAvis | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ avis }) => {
      this.avis = avis;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
