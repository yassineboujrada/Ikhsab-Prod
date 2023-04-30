export interface IGroups {
  id?: string;
  name?: string | null;
  userId?: string | null;
  gpsAdress?: string | null;
}

export const defaultValue: Readonly<IGroups> = {};
