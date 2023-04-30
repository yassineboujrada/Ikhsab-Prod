import dayjs from 'dayjs';

export interface IChaleurs {
  id?: string;
  date?: string | null;
  jrsLact?: string | null;
  temps?: string | null;
  groupeid?: string | null;
  enclosid?: string | null;
  activite?: string | null;
  facteurEleve?: string | null;
  suspect?: string | null;
  actAugmentee?: string | null;
  alarmeChaleur?: string | null;
  pasDeChaleur?: string | null;
  cowId?: string | null;
}

export const defaultValue: Readonly<IChaleurs> = {};
