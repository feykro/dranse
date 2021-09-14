import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-accueil',
  templateUrl: './accueil.component.html',
  styleUrls: ['./accueil.component.scss'],
})
export class AccueilComponent implements OnInit {
  public idList = [1, 2, 3, 4, 5];

  constructor(private router: Router) {
    const a = 0;
  }

  ngOnInit(): void {
    const b = 1;
  }

  navigate(n: number): void {
    this.router.navigate(['/produit', this.idList[n]]);
  }
}
