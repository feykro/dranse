import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-recherche',
  templateUrl: './page-recherche.component.html',
  styleUrls: ['./page-recherche.component.scss'],
})
export class PageRechercheComponent implements OnInit {
  resultats: resultatItem[] = [];
  //todo: récupérer la recherche dans l'url or something

  constructor() {
    //  shutting down warnings

    const b = 0;
  }

  ngOnInit(): void {
    this.fakeResultsGen();
    const a = 0;
  }

  resultGeneration(): void {
    //  step 1 : connect to API
    //  step 2 : get n-th top result
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
