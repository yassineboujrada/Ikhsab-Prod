import dayjs from 'dayjs';

export interface ISante {
  id?: string;
  date?: string | null;
  dureePositionCouchee?: string | null;
  leve?: string | null;
  pas?: string | null;
  cowId?: string | null;
}

export const defaultValue: Readonly<ISante> = {};
