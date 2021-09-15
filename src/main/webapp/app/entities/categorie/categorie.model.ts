export interface ICategorie {
  id?: number;
  nom?: string | null;
  description?: string | null;
}

export class Categorie implements ICategorie {
  constructor(public id?: number, public nom?: string | null, public description?: string | null) {}
}

export function getCategorieIdentifier(categorie: ICategorie): number | undefined {
  return categorie.id;
}
