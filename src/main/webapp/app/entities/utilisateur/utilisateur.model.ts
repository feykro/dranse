import { ICommande } from 'app/entities/commande/commande.model';

export interface IUtilisateur {
  id?: number;
  mail?: string | null;
  motDePasse?: string | null;
  nom?: string | null;
  prenom?: string | null;
  adrRue?: string | null;
  adrCodePostal?: number | null;
  adrPays?: string | null;
  adrVille?: string | null;
  telephone?: string | null;
  numCB?: string | null;
  commandes?: ICommande[] | null;
}

export class Utilisateur implements IUtilisateur {
  constructor(
    public id?: number,
    public mail?: string | null,
    public motDePasse?: string | null,
    public nom?: string | null,
    public prenom?: string | null,
    public adrRue?: string | null,
    public adrCodePostal?: number | null,
    public adrPays?: string | null,
    public adrVille?: string | null,
    public telephone?: string | null,
    public numCB?: string | null,
    public commandes?: ICommande[] | null
  ) {}
}

export function getUtilisateurIdentifier(utilisateur: IUtilisateur): number | undefined {
  return utilisateur.id;
}
