import {Message} from './Message';

export interface TemplateMessage extends Message {
  templateName: string;
}
