import * as dayjs from 'dayjs';
import { ILivre } from 'app/entities/livre/livre.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface IAvis {
  id?: number;
  note?: number | null;
  commentaire?: string | null;
  datePublication?: dayjs.Dayjs | null;
  affiche?: boolean | null;
  livre?: ILivre | null;
  utilisateur?: IUtilisateur | null;
}

export class Avis implements IAvis {
  constructor(
    public id?: number,
    public note?: number | null,
    public commentaire?: string | null,
    public datePublication?: dayjs.Dayjs | null,
    public affiche?: boolean | null,
    public livre?: ILivre | null,
    public utilisateur?: IUtilisateur | null
  ) {
    this.affiche = this.affiche ?? false;
  }
}

export function getAvisIdentifier(avis: IAvis): number | undefined {
  return avis.id;
}
