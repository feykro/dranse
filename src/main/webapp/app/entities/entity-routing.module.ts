import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'utilisateur',
        data: { pageTitle: 'projetDranseApp.utilisateur.home.title' },
        loadChildren: () => import('./utilisateur/utilisateur.module').then(m => m.UtilisateurModule),
      },
      {
        path: 'commande',
        data: { pageTitle: 'projetDranseApp.commande.home.title' },
        loadChildren: () => import('./commande/commande.module').then(m => m.CommandeModule),
      },
      {
        path: 'ligne-commande',
        data: { pageTitle: 'projetDranseApp.ligneCommande.home.title' },
        loadChildren: () => import('./ligne-commande/ligne-commande.module').then(m => m.LigneCommandeModule),
      },
      {
        path: 'livre',
        data: { pageTitle: 'projetDranseApp.livre.home.title' },
        loadChildren: () => import('./livre/livre.module').then(m => m.LivreModule),
      },
      {
        path: 'categorie',
        data: { pageTitle: 'projetDranseApp.categorie.home.title' },
        loadChildren: () => import('./categorie/categorie.module').then(m => m.CategorieModule),
      },
      {
        path: 'avis',
        data: { pageTitle: 'projetDranseApp.avis.home.title' },
        loadChildren: () => import('./avis/avis.module').then(m => m.AvisModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
