export interface IEnclos {
  id?: string;
  name?: string | null;
  userId?: string | null;
  groupId?: string | null;
}

export const defaultValue: Readonly<IEnclos> = {};
