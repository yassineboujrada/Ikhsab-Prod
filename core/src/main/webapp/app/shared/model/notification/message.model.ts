import dayjs from 'dayjs';
import { MessageType } from 'app/shared/model/enumerations/message-type.model';

export interface IMessage {
  id?: string;
  createdDateTime?: string | null;
  messageType?: MessageType | null;
  content?: string | null;
  room?: string | null;
  username?: string | null;
}

export const defaultValue: Readonly<IMessage> = {};
