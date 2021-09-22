import { ICategorie } from './../../entities/categorie/categorie.model';
import { Observable } from 'rxjs';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { GetBookControllerRessourceService } from '../service/get-book-controller-ressource.service';
import { HttpResponse } from '@angular/common/http';
import { ILivre } from 'app/entities/livre/livre.model';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Location } from '@angular/common';

@Component({
  selector: 'jhi-recherche',
  templateUrl: './page-recherche.component.html',
  styleUrls: ['./page-recherche.component.scss'],
})
export class PageRechercheComponent implements OnInit {
  resultats: resultatItem[] = [];
  livrePage: ILivre[] = [];

  pageSize = 20;

  nbResult = 0; //  Variable pour l'affichage des pages "du fond"

  typeRecherche!: string; //  aut ou cat
  pageRecherche!: number;
  argumentRecherche!: string; //  nom du livre ou de l'auteur
  titlePage = 'Résultat pour: ';

  constructor(
    private activeRoute: ActivatedRoute,
    private getBookController: GetBookControllerRessourceService,
    private router: Router,
    private loc: Location
  ) {
    //  shutting down warnings
    const b = 0;
  }

  ngOnInit(): void {
    //this.fakeResultsGen();

    this.activeRoute.params.subscribe((params: Params) => {
      this.typeRecherche = params['type'];
      this.pageRecherche = params['page'] - 1;
      this.argumentRecherche = params['arg'];

      if (this.typeRecherche === 'cat') {
        this.titlePage = this.titlePage + '(catégorie) ' + this.argumentRecherche;
        this.populatePageCat();
      } else {
        this.titlePage = this.titlePage + this.argumentRecherche;
        this.populatePageCherche();
      }
    });
  }

  populatePageCat(): void {
    const livreRqst: Observable<HttpResponse<ILivre[]>> = <Observable<HttpResponse<ILivre[]>>>(
      this.getBookController.getPageParCategorie(this.argumentRecherche, this.pageRecherche, this.pageSize)
    );
    livreRqst.subscribe(data => {
      this.livrePage = <ILivre[]>data.body;
      this.nbResult = this.livrePage.length;
    });
  }

  populatePageCherche(): void {
    const livreRqst: Observable<HttpResponse<ILivre[]>> = <Observable<HttpResponse<ILivre[]>>>(
      this.getBookController.getPageRecherche(this.argumentRecherche, this.pageRecherche, this.pageSize)
    );
    livreRqst.subscribe(data => {
      this.livrePage = <ILivre[]>data.body;
      this.nbResult = this.livrePage.length;
    });
  }

  gotoLivre(bookID?: number): void {
    this.router.navigate(['/produit', bookID]);
  }

  gotoNext(): void {
    let numeroPage: number = +this.pageRecherche;
    numeroPage += 2;
    const url = '/recherche/' + this.typeRecherche + '/' + this.argumentRecherche + '/' + numeroPage.toString();
    //this.router.navigate(['/recherche', this.typeRecherche, this.argumentRecherche, numeroPage.toString()]);

    this.router.navigateByUrl(url).then(() => window.location.reload());
  }

  gotoPrevious(): void {
    this.router
      .navigate(['/recherche', this.typeRecherche, this.argumentRecherche, this.pageRecherche])
      .then(() => window.location.reload());
  }

  goBack(): void {
    this.loc.back();
  }

  gotoHome(): void {
    this.router.navigate(['']);
  }

  fakeResultsGen(): void {
    const fakeURL = 'https://images-na.ssl-images-amazon.com/images/I/61HuQRNiZyL.jpg';
    const fakeItem = new resultatItem(fakeURL, 'Dune', 'Frank Herbert', 'Science-Fiction', '14,99');

    for (let i = 0; i < 5; i++) {
      console.log('In boucle', i);
      this.resultats.push(fakeItem);
    }
  }
}

export class resultatItem {
  constructor(public urlString: string, public titre: string, public auteur: string, public genre: string, public prix: string) {
    // salut
  }
}
