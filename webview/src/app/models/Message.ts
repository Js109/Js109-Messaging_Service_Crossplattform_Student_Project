import {LocationData} from './LocationData';

export interface Message {
  topic: string;
  sender: string;
  title: string;
  properties: string[];
  content: string;
  links: string[];
  starttime: string;
  attachment: number[];
  locationData: LocationData | null;
}
