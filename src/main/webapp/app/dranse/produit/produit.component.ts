import { CommandeControllerRessourceService } from './../service/commande-controller-ressource.service';
import { ILivre } from 'app/entities/livre/livre.model';
import { LigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { LivreService } from 'app/entities/livre/service/livre.service';
import { Observable } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { PanierService } from '../panier/panier.service';

@Component({
  selector: 'jhi-produit',
  templateUrl: './produit.component.html',
  styleUrls: ['./produit.component.scss'],
})
export class ProduitComponent implements OnInit {
  public livreId = 0;
  public livreInfo!: ILivre;
  public ligneCommande!: LigneCommande;

  constructor(
    private activatedRoute: ActivatedRoute,
    private livreService: LivreService,
    private panierService: PanierService,
    private commandeService: CommandeControllerRessourceService
  ) {}

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
    //TODO Ajouter la livreId dans un objet Panier du frontEnd
    const panierId = this.panierService.getPanierId();
    this.ligneCommande.livre = this.livreInfo;
    this.ligneCommande.prixPaye = this.livreInfo.prix;
    this.ligneCommande.quantite = 1;
    if (panierId === -1) {
      this.panierService.creationCommande(this.ligneCommande);
    } else {
      this.panierService.ajoutLigne(this.ligneCommande);
    }
    confirm('Le produit a été ajouté au panier');
    //alert("Le livre n'a pas été trouvé ou n'est plus en stock");
  }
}
