import { CommandeControllerRessourceService } from './../service/commande-controller-ressource.service';
import { ILigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';
import { Component, OnInit } from '@angular/core';
import { PanierService } from './panier.service';
import { ICommande } from 'app/entities/commande/commande.model';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-panier',
  templateUrl: './panier.component.html',
  styleUrls: ['./panier.component.scss'],
})
export class PanierComponent implements OnInit {
  panierId!: number;
  lignes!: ILigneCommande[];
  prixTotal = 0;

  urlTest =
    'https://kbimages1-a.akamaihd.net/93affabc-5161-421e-80d5-4477a07b8cee/353/569/90/False/harry-potter-and-the-philosopher-s-stone-3.jpg';

  itemListCommande: itemCommande[] = [];

  constructor(private panierService: PanierService, public commandeService: CommandeControllerRessourceService) {
    this.panierId = this.panierService.getPanierId();
  }

  ngOnInit(): void {
    //  this.getLignesCommande();
    this.fakeCommandeInit(); //  Pour tester avec des faux livre
  }

  updatePrice(qte: number, itemPrice: string): void {
    const itemPrix: number = +itemPrice;
    this.prixTotal += qte * itemPrix;
    this.prixTotal = +this.prixTotal.toFixed(2);
  }

  /**
   * Converti un objet commande en liste d'itemCommande qu'on affiche
   */
  convertCommande(): void {
    const iterableLignes = Object.entries(this.lignes);
    let i = 0;
    for (const [, ligne] of iterableLignes) {
      this.itemListCommande.push(this.convertLigneCommande(ligne, i));
      i++;
    }
  }

  getLignesCommande(): void {
    const commandeRequest: Observable<HttpResponse<ICommande>> = <Observable<HttpResponse<ICommande>>>(
      this.commandeService.getCommande(this.panierService.getPanierId())
    );
    commandeRequest.subscribe(value => {
      const test = value.body?.ligneCommandes;
      if (test === undefined || test === null) {
        this.fakeCommandeInit();
      } else {
        this.lignes = test;
        this.convertCommande();
      }
    });
  }

  convertLigneCommande(ligne: ILigneCommande, position: number): itemCommande {
    let bookTitle = '';
    let bookAuthor = '';
    let bookPrice = '';
    let quantite = 0;

    const book = ligne.livre;

    book?.titre === undefined || book.titre === null ? (bookTitle = 'undefinedTitled') : (bookTitle = book.titre);
    book?.auteur === undefined || book.auteur === null ? (bookAuthor = 'underfinedAuthor') : (bookAuthor = book.auteur);
    ligne.quantite === undefined || ligne.quantite === null ? (quantite = 0) : (quantite = ligne.quantite);

    if (!(book?.prix === undefined || book.prix === null)) {
      this.prixTotal += book.prix;
      this.prixTotal = +this.prixTotal.toFixed(2);
      bookPrice = book.prix.toString();
    }

    return new itemCommande(this.urlTest, bookTitle, bookAuthor, bookPrice, quantite, position);
  }

  /**
   * Créer une commande de placeholder
   */
  fakeCommandeInit(): void {
    for (let i = 0; i < 4; i++) {
      const itemTest = new itemCommande(this.urlTest, 'Harry Potter', 'Nicolas Sarkozy', '36.99', 1, i);
      this.itemListCommande.push(itemTest);
      const x: number = +itemTest.prix;
      this.prixTotal += x * itemTest.qte;
      this.prixTotal = +this.prixTotal.toFixed(2);
    }
  }
}

/**
 * Cette classe permet de peupler une liste et d'afficher les objets contenus par la commande
 */
class itemCommande {
  constructor(
    public urlImage: string,
    public titre: string,
    public auteur: string,
    public prix: string,
    public qte: number,
    public position: number
  ) {
    //  shutting down warnings
  }

  inscreaseQte(): void {
    this.qte++;
  }

  decreaseQte(): void {
    this.qte--;
  }
}
