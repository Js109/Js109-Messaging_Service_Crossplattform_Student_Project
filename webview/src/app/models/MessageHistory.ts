import {Message} from './Message';

export interface MessageHistory extends Message{
  starttime_period?: string;
  endtime_period?: string;
}
