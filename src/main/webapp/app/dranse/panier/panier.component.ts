import { ILivre, Livre } from './../../entities/livre/livre.model';
import { ILigneCommande, LigneCommande } from './../../entities/ligne-commande/ligne-commande.model';
import { Component, OnInit } from '@angular/core';
import { PanierService } from './panier.service';
import { Commande } from 'app/entities/commande/commande.model';

@Component({
  selector: 'jhi-panier',
  templateUrl: './panier.component.html',
  styleUrls: ['./panier.component.scss'],
})
export class PanierComponent implements OnInit {
  panierId!: number;
  commande!: Commande;

  itemListTest: testCommande[] = [];
  urlTest =
    'https://kbimages1-a.akamaihd.net/93affabc-5161-421e-80d5-4477a07b8cee/353/569/90/False/harry-potter-and-the-philosopher-s-stone-3.jpg';

  liste = ['bonjour', 'salut', 'hey'];

  constructor(private panierService: PanierService) {
    this.panierId = this.panierService.getPanierId();
    this.commande = this.panierService.getCommande();

    const itemTest = new testCommande(this.urlTest, 'Harry Potter', 'Nicolas Sarkozy', '36.99', 1);

    for (let i = 0; i < 4; i++) {
      this.itemListTest.push(itemTest);
    }
  }

  ngOnInit(): void {
    console.log('');
  }
}

class testCommande {
  constructor(public urlImage: string, public titre: string, public auteur: string, public prix: string, public qte: number) {
    const a = 0;
  }
}
