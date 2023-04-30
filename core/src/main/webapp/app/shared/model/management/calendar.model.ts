import dayjs from 'dayjs';

export interface ICalendar {
  id?: string;
  lactation?: string | null;
  jrsLact?: string | null;
  statutReproduction?: string | null;
  etatProd?: string | null;
  dateNaissance?: string | null;
  velage?: string | null;
  chaleur?: string | null;
  insemination?: string | null;
  cowId?: string | null;
}

export const defaultValue: Readonly<ICalendar> = {};
