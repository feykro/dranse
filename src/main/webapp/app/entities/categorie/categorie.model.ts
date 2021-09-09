import { ILivre } from 'app/entities/livre/livre.model';

export interface ICategorie {
  id?: number;
  nom?: string | null;
  description?: string | null;
  livres?: ILivre[] | null;
}

export class Categorie implements ICategorie {
  constructor(public id?: number, public nom?: string | null, public description?: string | null, public livres?: ILivre[] | null) {}
}

export function getCategorieIdentifier(categorie: ICategorie): number | undefined {
  return categorie.id;
}
