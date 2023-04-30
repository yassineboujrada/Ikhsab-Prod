export interface ICow {
  id?: string;
  numero?: string | null;
  groupeId?: string | null;
  enclosId?: string | null;
  repondeur?: string | null;
  nom?: string | null;
  deviceId?: string | null;
  userId?: string | null;
}

export const defaultValue: Readonly<ICow> = {};
