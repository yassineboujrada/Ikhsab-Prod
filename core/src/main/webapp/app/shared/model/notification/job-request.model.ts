export interface IJobRequest {
  id?: string;
  consumer?: string | null;
  provider?: string | null;
  serviceStatus?: string | null;
  roomId?: string | null;
}

export const defaultValue: Readonly<IJobRequest> = {};
