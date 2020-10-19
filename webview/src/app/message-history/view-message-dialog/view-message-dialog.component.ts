import { Component, OnInit } from '@angular/core';
import {fontFamilyToFontString} from '../../models/FontFamily';
import {alignmentToAlignmentString} from '../../models/Alignment';
import {Message} from '../../models/Message';

@Component({
  selector: 'app-view-message-dialog',
  templateUrl: './view-message-dialog.component.html',
  styleUrls: ['./view-message-dialog.component.css']
})
export class ViewMessageDialogComponent implements OnInit {

  constructor() { }

  messageVal;
  get message(): Message {
    return this.messageVal;
  }
  set message(message: Message) {
    this.messageVal = message;
    this.expirationFromMessage();
  }
  fontFamilyToFontString = fontFamilyToFontString;
  alignmentToAlignmentString = alignmentToAlignmentString;

  expirationString = '';

  ngOnInit(): void {
    if (this.message.messageDisplayProperties == null){
      this.message.messageDisplayProperties = {};
    }
  }

  expirationFromMessage(): void {
    if (this.message.starttime != null && this.message.endtime != null
      && this.message.starttime.length !== 0 && this.message.endtime.length !== 0) {
      const millis = Date.parse(this.message.endtime) - Date.parse(this.message.starttime);

      const millisInAMinute = 60 * 1000;
      const millisInAnHour = 60 * millisInAMinute;
      const millisInADay = 24 * millisInAnHour;
      const millisInAWeek = 7 * millisInADay;

      if (millis % millisInAWeek === 0) {
        this.expirationString = (millis / millisInAWeek) + ' Weeks';
      } else if (millis % millisInADay === 0) {
        this.expirationString = (millis / millisInAWeek) + ' Days';
      } else if (millis % millisInAnHour === 0) {
        this.expirationString = (millis / millisInAnHour) + ' Hours';
      } else if (millis % millisInAMinute === 0) {
        this.expirationString = (millis / millisInAMinute) + ' Minutes';
      }
    }
  }

  /**
   * Create a data url from the base64 image data.
   * This url can be set as the source for an image in html/css to display it.
   * @param imageData image data in base 64
   */
  getDataUrlFromImageByteArray(imageData: string): string {
    return 'data:image/png;base64,' + imageData;
  }

}
