import * as dayjs from 'dayjs';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { ILigneCommande } from 'app/entities/ligne-commande/ligne-commande.model';

export interface ICommande {
  id?: number;
  date?: dayjs.Dayjs | null;
  paysLivraison?: string | null;
  codePostalLivraison?: number | null;
  villeLivraison?: string | null;
  rueLivraison?: string | null;
  nomLivraison?: string | null;
  paysFacturation?: string | null;
  codePostalFacturation?: string | null;
  villeFacturation?: string | null;
  rueFacturation?: string | null;
  nomFacturation?: string | null;
  utilisateur?: IUtilisateur | null;
  ligneCommandes?: ILigneCommande[] | null;
}

export class Commande implements ICommande {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public paysLivraison?: string | null,
    public codePostalLivraison?: number | null,
    public villeLivraison?: string | null,
    public rueLivraison?: string | null,
    public nomLivraison?: string | null,
    public paysFacturation?: string | null,
    public codePostalFacturation?: string | null,
    public villeFacturation?: string | null,
    public rueFacturation?: string | null,
    public nomFacturation?: string | null,
    public utilisateur?: IUtilisateur | null,
    public ligneCommandes?: ILigneCommande[] | null
  ) {}
}

export function getCommandeIdentifier(commande: ICommande): number | undefined {
  return commande.id;
}
