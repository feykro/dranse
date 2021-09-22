import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommandeControllerRessourceService } from '../service/commande-controller-ressource.service';
import { HistoriqueService } from './historique.service';
import { ICommande } from 'app/entities/commande/commande.model';
import * as dayjs from 'dayjs';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-historique',
  templateUrl: './historique.component.html',
  styleUrls: ['./historique.component.scss']
})
export class HistoriqueComponent implements OnInit  {
  historiqueId !: number;

  public historique !: any[];



  constructor(private historiqueService: HistoriqueService, public commandService: CommandeControllerRessourceService, private router: Router) {
    this.historiqueId = this.historiqueService.getHistoriqueId();

  }

  ngOnInit() : void {
   this.historiqueService.getHistorique().subscribe(value => {
            this.historique = <ICommande[]>value.body;

     });
  }

  gotoItem(id: number): void {
            this.router.navigate(['/produit', id]);
  }

  displayDate(date: dayjs.Dayjs): string {
    return dayjs(date).toString();
  }

  significantDigits(price : number): string {
    return price.toFixed(2);
  }

  sumCommand(command : ICommande) : number {

    let sum = 0;
    for (const item of command.ligneCommandes!){
      sum += item.prixPaye!;
    }
    return sum;

    /*
    const commandeRequest: Observable<HttpResponse<ICommande>> = <Observable<HttpResponse<ICommande>>>(
          this.commandService.getCommande(id)
        );
        commandeRequest.subscribe(value => {
          var sum = 0;
          for (let item of <ILigneCommande[]>value.body.ligneCommandes){
            sum += item.prixPaye!;
          }
        });
        return sum;*/
  }






}
