import { CommandeControllerRessourceService } from './../service/commande-controller-ressource.service';
import { ILivre } from 'app/entities/livre/livre.model';
import { LigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { LivreService } from 'app/entities/livre/service/livre.service';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { PanierService } from '../panier/panier.service';
import { ICommande } from 'app/entities/commande/commande.model';

@Component({
  selector: 'jhi-produit',
  templateUrl: './produit.component.html',
  styleUrls: ['./produit.component.scss'],
})
export class ProduitComponent implements OnInit {
  public livreId = 0;
  public livreInfo!: ILivre;

  message !: string;

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
    const bouquinRequest: Observable<HttpResponse<ILivre>> = <Observable<HttpResponse<ILivre>>>this.livreService.find(this.livreId);
    bouquinRequest.subscribe(value => {
      this.livreInfo = <ILivre>value.body;
    });
    this.panierService.fetchMessage().subscribe(message => this.message = message)

  }

  newMessage(message : string) : void {
      //const ret = Number(this.message)+1;
      this.panierService.sendMessage(message);
  }

  ajoutPanier(): void {
    const panierId = this.panierService.getPanierId();
    const lignecommande = new LigneCommande(undefined, 1, this.livreInfo.prix, undefined, this.livreInfo);
    console.log(panierId);
    if (panierId === -1) {
      this.panierService.creationCommande(lignecommande);
    } else {
      const commandeRequest: Observable<HttpResponse<ICommande>> = <Observable<HttpResponse<ICommande>>>(
        this.commandeService.getCommande(this.panierService.getPanierId())
      );
      commandeRequest.subscribe(value => {
        if (value.body === null) {
          alert("Votre panier a été nettoyé en raison d'une inactivité prolongée\nCe livre sera placé dans un nouveau panier");
          this.panierService.clearId();
          this.panierService.creationCommande(lignecommande);
          this.panierService.ajoutLigne(lignecommande);
        } else {
          this.panierService.ajoutLigne(lignecommande);
        }
      });
    }

  }
}
