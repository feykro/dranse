import { ILivre } from 'app/entities/livre/livre.model';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { LivreService } from 'app/entities/livre/service/livre.service';
import { Observable } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-produit',
  templateUrl: './produit.component.html',
  styleUrls: ['./produit.component.scss'],
})
export class ProduitComponent implements OnInit {
  public livreId = 0;
  public livreInfo!: ILivre;

  constructor(private activatedRoute: ActivatedRoute, private livreService: LivreService) {}

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
    console.log('AJOUTER PANIER');
  }
}
