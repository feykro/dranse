import { ICategorie } from 'app/entities/categorie/categorie.model';

export interface ILivre {
  id?: number;
  titre?: string | null;
  auteur?: string | null;
  prix?: number | null;
  synopsis?: string | null;
  edition?: number | null;
  anneePublication?: number | null;
  editeur?: string | null;
  stock?: number | null;
  categories?: ICategorie[] | null;
}

export class Livre implements ILivre {
  constructor(
    public id?: number,
    public titre?: string | null,
    public auteur?: string | null,
    public prix?: number | null,
    public synopsis?: string | null,
    public edition?: number | null,
    public anneePublication?: number | null,
    public editeur?: string | null,
    public stock?: number | null,
    public categories?: ICategorie[] | null
  ) {}
}

export function getLivreIdentifier(livre: ILivre): number | undefined {
  return livre.id;
}
