import {LocationData} from './LocationData';
import {FontFamily} from './FontFamily';

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
  attachment: number[];
  locationData: LocationData | null;
  fontFamily?: FontFamily;
  fontColor?: string;
  backgroundColor?: string;
}
