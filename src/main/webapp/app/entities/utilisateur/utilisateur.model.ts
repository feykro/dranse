import { ICommande } from 'app/entities/commande/commande.model';
import { IUser } from 'app/entities/user/user.model';

export interface IUtilisateur {
  id?: number;
  adrRue?: string | null;
  adrCodePostal?: number | null;
  adrPays?: string | null;
  adrVille?: string | null;
  telephone?: string | null;
  numCB?: string | null;
  commandes?: ICommande[] | null;
  user?: IUser | null;
}

export class Utilisateur implements IUtilisateur {
  constructor(
    public id?: number,
    public adrRue?: string | null,
    public adrCodePostal?: number | null,
    public adrPays?: string | null,
    public adrVille?: string | null,
    public telephone?: string | null,
    public numCB?: string | null,
    public commandes?: ICommande[] | null,
    public user?: IUser | null
  ) {}
}

export function getUtilisateurIdentifier(utilisateur: IUtilisateur): number | undefined {
  return utilisateur.id;
}
