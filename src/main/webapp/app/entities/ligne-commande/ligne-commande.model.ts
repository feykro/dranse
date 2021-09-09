import { ICommande } from 'app/entities/commande/commande.model';
import { ILivre } from 'app/entities/livre/livre.model';

export interface ILigneCommande {
  id?: number;
  quantite?: number | null;
  prixPaye?: number | null;
  commande?: ICommande | null;
  livre?: ILivre | null;
}

export class LigneCommande implements ILigneCommande {
  constructor(
    public id?: number,
    public quantite?: number | null,
    public prixPaye?: number | null,
    public commande?: ICommande | null,
    public livre?: ILivre | null
  ) {}
}

export function getLigneCommandeIdentifier(ligneCommande: ILigneCommande): number | undefined {
  return ligneCommande.id;
}
