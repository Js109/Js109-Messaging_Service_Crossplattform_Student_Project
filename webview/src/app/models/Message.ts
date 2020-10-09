import {LocationData} from './LocationData';
import {MessageDisplayProperties} from './MessageDisplayProperties';

export interface Message {
  id?: number;
  topic: string;
  sender: string;
  title: string;
  properties: string[];
  content: string;
  links: string[];
  starttime: string;
  isSent: boolean;
  endtime: string;
  attachment: string;
  locationData: LocationData | null;
  logoAttachment: string;
  messageDisplayProperties: MessageDisplayProperties | null;
}
