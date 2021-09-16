import { ILivre } from 'app/entities/livre/livre.model';
import { LigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { LivreService } from 'app/entities/livre/service/livre.service';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { PanierService } from '../panier/panier.service';

@Component({
  selector: 'jhi-produit',
  templateUrl: './produit.component.html',
  styleUrls: ['./produit.component.scss'],
})
export class ProduitComponent implements OnInit {
  public livreId = 0;
  public livreInfo!: ILivre;

  constructor(private activatedRoute: ActivatedRoute, private livreService: LivreService, private panierService: PanierService) {}

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((params: Params) => {
      this.livreId = params['livreId'];
    });
    console.log(this.livreId);
    const bouquinRequest: Observable<HttpResponse<ILivre>> = <Observable<HttpResponse<ILivre>>>this.livreService.find(this.livreId);
    bouquinRequest.subscribe(value => {
      this.livreInfo = <ILivre>value.body;
    });
  }

  ajoutPanier(): void {
    const panierId = this.panierService.getPanierId();
    const lignecommande = new LigneCommande(undefined, 1, this.livreInfo.prix, undefined, this.livreInfo);
    console.log(panierId);
    if (panierId === -1) {
      this.panierService.creationCommande(lignecommande);
    } else {
      this.panierService.ajoutLigne(lignecommande);
    }
  }
}
