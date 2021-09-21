import { ICategorie } from './../../entities/categorie/categorie.model';
import { Observable } from 'rxjs';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { GetBookControllerRessourceService } from '../service/get-book-controller-ressource.service';
import { HttpResponse } from '@angular/common/http';
import { ILivre } from 'app/entities/livre/livre.model';

@Component({
  selector: 'jhi-recherche',
  templateUrl: './page-recherche.component.html',
  styleUrls: ['./page-recherche.component.scss'],
})
export class PageRechercheComponent implements OnInit {
  resultats: resultatItem[] = [];
  livrePage: ILivre[] = [];

  pageSize = 20;

  typeRecherche!: string; //  aut ou cat
  pageRecherche!: number;
  argumentRecherche!: string; //  nom du livre ou de l'auteur
  titlePage = 'Résultat pour: ';

  constructor(private activeRoute: ActivatedRoute, private getBookController: GetBookControllerRessourceService, private router: Router) {
    //  shutting down warnings
    const b = 0;
  }

  ngOnInit(): void {
    //this.fakeResultsGen();

    this.activeRoute.params.subscribe((params: Params) => {
      this.typeRecherche = params['type'];
      this.pageRecherche = params['page'];
      this.argumentRecherche = params['arg'];

      if (this.typeRecherche === 'cat') {
        this.titlePage = this.titlePage + '(catégorie) ';
      }
      this.titlePage = this.titlePage + this.argumentRecherche;
    });

    //  todo: mettre des tests et des valeurs par défaut pour la robustesse

    const livreRqst: Observable<HttpResponse<ILivre[]>> = <Observable<HttpResponse<ILivre[]>>>(
      this.getBookController.getPageParCategorie(this.argumentRecherche, this.pageRecherche, this.pageSize)
    );
    livreRqst.subscribe(data => (this.livrePage = <ILivre[]>data.body));
  }

  gotoLivre(bookID?: number): void {
    this.router.navigate(['/produit', bookID]);
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
