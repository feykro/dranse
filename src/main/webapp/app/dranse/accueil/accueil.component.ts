import { Search } from './../../core/request/request.model';
import { IconDefinition } from '@fortawesome/free-solid-svg-icons';
import { ICategorie } from './../../entities/categorie/categorie.model';
import { CategorieService } from './../../entities/categorie/service/categorie.service';
import { ILivre } from 'app/entities/livre/livre.model';
import { LivreService } from 'app/entities/livre/service/livre.service';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { CategoriesControllerRessourceService } from '../service/categories-controller-ressource.service';

@Component({
  selector: 'jhi-accueil',
  templateUrl: './accueil.component.html',
  styleUrls: ['./accueil.component.scss'],
})
export class AccueilComponent implements OnInit {
  public nbBS = 5; //  nombre de best seller
  public nbCat = 12;
  public bestSellers = new Array<ILivre>();
  public popularCategories: ICategorie[] = [];

  constructor(private router: Router, private bookService: LivreService, private categoriesService: CategoriesControllerRessourceService) {
    // shutting down warnings
  }

  ngOnInit(): void {
    this.getBestSellers();
    this.getTopCategories();
  }

  navigate(n: number): void {
    this.router.navigate(['/produit', n]);
  }

  getBestSellers(): void {
    for (let i = 0; i < this.nbBS; i++) {
      const bouquinRequest: Observable<HttpResponse<ILivre[]>> = this.bookService.getBestSeller();
      bouquinRequest.subscribe(data => {
        this.bestSellers = <ILivre[]>data.body;
      });
    }
  }

  getTopCategories(): void {
    const catRequest: Observable<HttpResponse<ICategorie[]>> = <Observable<HttpResponse<ICategorie[]>>>(
      this.categoriesService.getPopularCategories()
    );
    catRequest.subscribe(data => {
      this.popularCategories = <ICategorie[]>data.body;
      while (this.popularCategories.length > this.nbCat) {
        this.popularCategories.pop();
      }
    });
  }

  gotoSearch(cat: string): void {
    this.router.navigate(['/recherche', 'cat', cat, 1]);
  }
}
