import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommandeControllerRessourceService } from '../service/commande-controller-ressource.service';
import { HistoriqueService } from './historique.service';
import { ICommande } from 'app/entities/commande/commande.model';
import * as dayjs from 'dayjs';

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






}
