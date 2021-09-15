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

  constructor(private panierService: PanierService) {
    this.panierId = this.panierService.getPanierId();
    this.commande = this.panierService.getCommande();
  }

  ngOnInit(): void {
    console.log('');
  }
}
