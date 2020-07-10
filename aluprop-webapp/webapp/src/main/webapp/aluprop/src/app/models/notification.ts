import { User } from './user';
import {Proposal} from "./proposal";

export class Notification {
  user: User;
  id: number;
  subjectCode: string;
  translatedSubject: string
  textCode: string;
  translatedText: string;
  link: string;
  state: NotificationState;
  proposal: Proposal;
}

export type NotificationState  = 'UNREAD' | 'READ';
export const NotificationState = {
  Unread: 'UNREAD' as NotificationState,
  Read: 'READ' as NotificationState
};
