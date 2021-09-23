import { Router } from '@angular/router';
import { CommandeControllerRessourceService } from './../service/commande-controller-ressource.service';
import { ILigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';
import { Component, OnInit } from '@angular/core';
import { PanierService } from './panier.service';
import { ICommande } from 'app/entities/commande/commande.model';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import { LigneCommandeService } from 'app/entities/ligne-commande/service/ligne-commande.service';

@Component({
  selector: 'jhi-panier',
  templateUrl: './panier.component.html',
  styleUrls: ['./panier.component.scss'],
})
export class PanierComponent implements OnInit {
  panierId!: number;
  lignes!: ILigneCommande[];
  prixTotal = 0;
  trash = faTrash;

  itemListCommande: itemCommande[] = [];

  message!: string;

  constructor(private panierService: PanierService, public commandeService: CommandeControllerRessourceService, private router: Router) {
    this.panierId = this.panierService.getPanierId();
  }

  ngOnInit(): void {
    const commandeRequest: Observable<HttpResponse<ICommande>> = <Observable<HttpResponse<ICommande>>>(
      this.commandeService.getCommande(this.panierService.getPanierId())
    );

    commandeRequest.subscribe(value => {
      this.panierService.fetchMessage().subscribe(message => (this.message = message));
      if (value.body === null && this.panierService.getPanierId() !== -1) {
        alert("Votre panier a été nettoyé en raison d'une inactivité prolongée");
        this.panierService.clearId();
      } else {
        this.getLignesCommande();
      }
    });
  }

  newMessage(): void {
    this.panierService.sendMessage(this.lignes.length.toString());
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
    this.updateTotalPrice();
    this.newMessage();
  }

  getLignesCommande(): void {
    const commandeRequest: Observable<HttpResponse<ICommande>> = <Observable<HttpResponse<ICommande>>>(
      this.commandeService.getCommande(this.panierService.getPanierId())
    );
    commandeRequest.subscribe(value => {
      const test = value.body?.ligneCommandes;
      if (test !== undefined && test !== null) {
        this.lignes = test;
        this.convertCommande();
      }
    });
    this.newMessage();
  }

  convertLigneCommande(ligne: ILigneCommande, position: number): itemCommande {
    let bookTitle = '';
    let bookAuthor = '';
    let bookPrice = '';
    let quantite = 0;
    let idLivre = 0;
    let urlImage = '';

    const book = ligne.livre;

    book?.titre === undefined || book.titre === null ? (bookTitle = 'undefinedTitled') : (bookTitle = book.titre);
    book?.auteur === undefined || book.auteur === null ? (bookAuthor = 'underfinedAuthor') : (bookAuthor = book.auteur);
    ligne.quantite === undefined || ligne.quantite === null ? (quantite = 0) : (quantite = ligne.quantite);
    book?.id === undefined || ligne.quantite === null ? (idLivre = 0) : (idLivre = book.id);
    book?.urlImage === undefined || book.urlImage === null ? (urlImage = 'Image') : (urlImage = book.urlImage);

    if (!(book?.prix === undefined || book.prix === null)) {
      this.prixTotal += book.prix;
      this.prixTotal = +this.prixTotal.toFixed(2);
      bookPrice = book.prix.toString();
    }
    this.newMessage();
    return new itemCommande(urlImage, bookTitle, bookAuthor, bookPrice, quantite, position, idLivre);
  }

  updateAll(): void {
    this.lignes = [];
    this.itemListCommande = [];
    this.getLignesCommande();
    this.updateTotalPrice();
    this.newMessage();
  }

  updateTotalPrice(): void {
    let prix = 0;
    for (const item of this.lignes) {
      const price = item.livre?.prix === null || item.livre?.prix === undefined ? 0 : item.livre.prix;
      const qte = item.quantite === null || item.quantite === undefined ? 0 : item.quantite;
      prix += price * qte;
    }
    this.prixTotal = prix;
    this.prixTotal = +this.prixTotal.toFixed(2);
    this.newMessage();
  }

  incrementerObjet(indice: number): void {
    const commandeRequest: Observable<HttpResponse<ICommande>> = <Observable<HttpResponse<ICommande>>>(
      this.commandeService.getCommande(this.panierService.getPanierId())
    );
    commandeRequest.subscribe(valueC => {
      if (valueC.body === null) {
        alert("Votre panier a été nettoyé en raison d'une inactivité prolongée");
        this.panierService.clearId();
        this.updateAll();
      } else {
        const newLigne = this.lignes[indice];
        if (newLigne.quantite === null || newLigne.quantite === undefined) {
          newLigne.quantite = 1;
        } else {
          newLigne.quantite += 1;
        }

        this.panierService.modifierLigne(newLigne).subscribe(value => {
          if (value.body !== null) {
            this.lignes[indice] = newLigne;
            this.updateAll();
          } else {
            alert("Le livre n'est plus en stock");
          }
        });
      }
    });
    this.newMessage();
  }

  decrementerObjet(indice: number): void {
    const commandeRequest: Observable<HttpResponse<ICommande>> = <Observable<HttpResponse<ICommande>>>(
      this.commandeService.getCommande(this.panierService.getPanierId())
    );
    commandeRequest.subscribe(valueC => {
      if (valueC.body === null) {
        alert("Votre panier a été nettoyé en raison d'une inactivité prolongée");
        this.panierService.clearId();
        this.updateAll();
      } else {
        const newLigne = this.lignes[indice];
        if (newLigne.quantite === null || newLigne.quantite === undefined) {
          newLigne.quantite = 1;
        } else if (newLigne.quantite <= 1) {
          const res = confirm('Voulez-vous vraiment supprimer ce livre de la commande ?');
          if (res === false) {
            return;
          }
          newLigne.quantite = 0;
        } else {
          newLigne.quantite -= 1;
        }

        this.panierService.modifierLigne(newLigne).subscribe(value => {
          if (value.body !== null) {
            this.lignes[indice] = newLigne;
          }
          this.updateAll();
        });
      }
    });
    this.newMessage();
  }

  gotoItem(id: number): void {
    this.router.navigate(['/produit', id]);
  }

  /**
   * Créer une commande de placeholder
   */
  fakeCommandeInit(): void {
    for (let i = 0; i < 4; i++) {
      const urlTest = '';
      const itemTest = new itemCommande(urlTest, 'Harry Potter', 'Nicolas Sarkozy', '36.99', 1, i, i);
      this.itemListCommande.push(itemTest);
      const x: number = +itemTest.prix;
      this.prixTotal += x * itemTest.qte;
      this.prixTotal = +this.prixTotal.toFixed(2);
    }
  }

  loadVerifPage(): void {
    if (this.panierService.getPanierId() === -1) {
      alert('Impossible de payer avec un panier vide');
    } else {
      const commandeRequest: Observable<HttpResponse<ICommande>> = <Observable<HttpResponse<ICommande>>>(
        this.commandeService.getCommande(this.panierService.getPanierId())
      );
      commandeRequest.subscribe(value => {
        const test = value.body?.ligneCommandes;
        if (value.body?.ligneCommandes?.length === 0) {
          alert('Vous devez avoir au moins un livre dans votre commande pour passer commande');
        } else {
          this.router.navigate(['/verification']);
        }
      });
      this.newMessage();
    }
  }

  supprimerLivre(pos: number): void {
    const commandeRequest: Observable<HttpResponse<ICommande>> = <Observable<HttpResponse<ICommande>>>(
      this.commandeService.getCommande(this.panierService.getPanierId())
    );
    commandeRequest.subscribe(valueC => {
      if (valueC.body === null) {
        alert("Votre panier a été nettoyé en raison d'une inactivité prolongée");
        this.panierService.clearId();
        this.updateAll();
      } else {
        const res = confirm('Voulez-vous vraiment supprimer ce livre de la commande ?');
        if (res === true) {
          const newLigne = this.lignes[pos];
          newLigne.quantite = 0;
          this.panierService.modifierLigne(newLigne).subscribe(value => {
            if (value.body !== null) {
              this.lignes[pos] = newLigne;
            }
            this.updateAll();
          });
        }
      }
    });
    this.newMessage();
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
    public position: number,
    public identifiant: number
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
