import { User } from './user';

export class Notification {
  user: User;
  id: number;
  subjectCode: string;
  subject: string
  textCode: string;
  text: string;
  link: string;
  state: NotificationState;
}

export type NotificationState  = 'UNREAD' | 'READ';
export const NotificationState = {
  Unread: 'UNREAD' as NotificationState,
  Read: 'READ' as NotificationState
};