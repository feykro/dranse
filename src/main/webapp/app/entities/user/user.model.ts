export interface IUser {
  id?: number;
  login?: string;
  password?: string;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
}

export class User implements IUser {
  constructor(
    public id: number,
    public login: string,
    public password: string,
    public firstName: string | null,
    public lastName: string | null,
    public email: string | null
  ) {}
}

export function getUserIdentifier(user: IUser): number | undefined {
  return user.id;
}
