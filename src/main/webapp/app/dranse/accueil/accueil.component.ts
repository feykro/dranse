import { ILivre } from 'app/entities/livre/livre.model';
import { LivreService } from 'app/entities/livre/service/livre.service';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-accueil',
  templateUrl: './accueil.component.html',
  styleUrls: ['./accueil.component.scss'],
})
export class AccueilComponent implements OnInit {
  public nbBS = 5; //  nombre de best seller
  public idList = [1, 2, 3, 4, 5];
  public bestSellers = new Array<ILivre>();
  //  public livre1!: ILivre;

  constructor(private router: Router, private bookService: LivreService) {
    // shutting down warnings
  }

  ngOnInit(): void {
    this.getBestSellers();
  }

  navigate(n: number): void {
    this.router.navigate(['/produit', this.idList[n]]);
  }

  getBestSellers(): void {
    //  (<Observable<HttpResponse<ILivre>>>this.bookService.find(1)).subscribe(data => (this.livre1 = <ILivre>data));

    for (let i = 0; i < this.nbBS; i++) {
      const bouquinRequest: Observable<HttpResponse<ILivre>> = <Observable<HttpResponse<ILivre>>>this.bookService.find(i + 1);

      bouquinRequest.subscribe(data => {
        this.bestSellers.push(<ILivre>data.body);
      });
    }
  }

  /*
  sortBooks(a: ILivre, b: ILivre): number {
    if (b.id === undefined) {
      return 1;
    }
    if (a.id === undefined) {
      return -1;
    }
    if (a.id > b.id) {
      return 1;
    }
    return -1;
  }
  */
}
