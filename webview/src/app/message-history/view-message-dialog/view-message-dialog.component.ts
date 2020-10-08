import { Component, OnInit } from '@angular/core';
import {fontFamilyToFontString} from '../../models/FontFamily';

@Component({
  selector: 'app-view-message-dialog',
  templateUrl: './view-message-dialog.component.html',
  styleUrls: ['./view-message-dialog.component.css']
})
export class ViewMessageDialogComponent implements OnInit {

  constructor() { }

  message;
  fontFamilyToFontString = fontFamilyToFontString;

  ngOnInit(): void {
    if (this.message.messageDisplayProperties == null){
      this.message.messageDisplayProperties = {};
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
