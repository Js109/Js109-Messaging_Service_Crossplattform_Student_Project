import {Component, Input, OnInit} from '@angular/core';
import {environment} from '../../../environments/environment';
import {Topic} from '../../models/Topic';
import {Property} from '../../models/Property';
import {HttpClient} from '@angular/common/http';
import {Message} from '../../models/Message';
import {LocationData} from '../../models/LocationData';
import {FontFamily, fontFamilyToFontString} from '../../models/FontFamily';
import {MatTabChangeEvent} from '@angular/material/tabs';
import {MatSlideToggleChange} from '@angular/material/slide-toggle';
import {Alignment, alignmentToAlignmentString} from '../../models/Alignment';

enum OffsetType {
  Minute, Hour, Day, Week
}

@Component({
  selector: 'app-message-form',
  templateUrl: './message-form.component.html',
  styleUrls: ['./message-form.component.css']
})
export class MessageFormComponent implements OnInit {

  constructor(private http: HttpClient) {
  }

  get message(): Message {
    return this.messageValue;
  }

  @Input()
  set message(val) {
    this.messageValue = val;
    if (this.message.messageDisplayProperties == null) {
      this.message.messageDisplayProperties = {};
    }
    if (val.locationData != null) {
      this.locationData = val.locationData;
    }
    if (val.messageDisplayProperties.fontColor == null || val.messageDisplayProperties.backgroundColor == null) {
      this.hasCustomColor = false;
      this.selectedFontColor = '#000000';
      this.selectedBackgroundColor = '#ffffff';
    } else {
      this.hasCustomColor = true;
    }
    this.expirationFromMessage();
    this.properties = this.properties.map(property => [property[0], val.properties.some(value => value === property[0].binding)]);
  }

  // copying of global enums/functions to local fields for accessing in the template
  fontFamilyToFontString = fontFamilyToFontString;
  alignmentToAlignmentString = alignmentToAlignmentString;
  alignment = Alignment;
  fontFamily = FontFamily;

  topics: Topic[];

  properties: [Property, boolean][] = [];

  removedTopic: string;

  messageValue: Message = {
    isSent: false,
    topic: '',
    properties: [],
    content: '',
    sender: '',
    title: '',
    links: [],
    starttime: '',
    endtime: '',
    attachment: '',
    logoAttachment: '',
    locationData: null,
    messageDisplayProperties: {}
  };

  locationData: LocationData = {radius: 50};
  expirationOffset: number;
  expirationOffsetType: OffsetType = null;
  linkCounter = 1;

  hasTopicPropertiesError = false;
  hasSenderError = false;
  hasTitleError = false;
  hasContentError = false;
  coordValueRangeError = false;
  onlyOneCoordError = false;
  urlErrors: boolean[] = [];
  hasUrlErrors;
  hasColorError = false;

  hasCustomColor = false;
  selectedFontColor = '#000000';
  selectedBackgroundColor = '#ffffff';

  ngOnInit(): void {
    this.http.get(environment.backendApiPath + '/topic', {responseType: 'json'})
      .subscribe((topics: Topic[]) => this.topics = topics);
    this.http.get(environment.backendApiPath + '/property', {responseType: 'json'})
      .subscribe((properties: Property[]) => this.properties = properties.map(value => [value, false]));
  }

  /**
   * Fills the message with all elements from the form and then performs validation on it.
   * If validation is passed the message will be returned otherwise null will be returned.
   * Use this to retrieve the message instead of using binding as this component does not set all values put into the form directly.
   * @return Message containing the information put into the form or null if validation fails.
   */
  retrieveMessage(): Message {
    if (this.validateInputs()) {
      this.setEndtimeFromExpirationOffset();
      const message: Message = {...this.message};
      if (this.displayPropertiesEmpty()) {
        message.messageDisplayProperties = null;
      }
      return message;
    }
    return null;
  }

  displayPropertiesEmpty(): boolean {
    for (const key in this.message.messageDisplayProperties) {
      if (this.message.messageDisplayProperties[key] != null) {
        return false;
      }
    }
    return true;
  }

  validateInputs(): boolean {
    this.hasTopicPropertiesError = this.message.topic === '' && this.message.properties.length === 0;
    this.hasSenderError = this.message.sender === '';
    this.hasTitleError = this.message.title === '';
    this.hasContentError = this.message.content === '' && this.message.attachment.length === 0;
    const locationData = this.message.locationData;
    if (locationData != null) {
      this.coordValueRangeError = locationData.lat < -90 || locationData.lat > 90 || locationData.lng < -180 || locationData.lng > 180;
      this.onlyOneCoordError = (locationData.lat == null && locationData.lng != null)
        || (locationData.lat != null && locationData.lng == null);
    }
    const urlRegex = new RegExp(
      '((http|https)\\/\\/)?(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)');
    this.urlErrors = this.message.links.map((url) => !urlRegex.test(url));
    this.hasUrlErrors = this.urlErrors.some((element) => element);
    this.hasColorError = !(
      (this.message.messageDisplayProperties.backgroundColor == null && this.message.messageDisplayProperties.fontColor == null)
      || this.message.messageDisplayProperties.backgroundColor !== this.message.messageDisplayProperties.fontColor
    );
    return !(this.hasTopicPropertiesError
      || this.hasSenderError
      || this.hasTitleError
      || this.hasContentError
      || this.coordValueRangeError
      || this.onlyOneCoordError
      || this.hasUrlErrors
      || this.hasColorError);
  }

  setBackgroundColor($event: string): void {
    this.message.messageDisplayProperties.backgroundColor = $event;
  }

  setFontColor($event: string): void {
    this.message.messageDisplayProperties.fontColor = $event;
  }

  enableCustomColor($event: MatSlideToggleChange): void {
    if ($event.checked) {
      this.message.messageDisplayProperties.fontColor = this.selectedFontColor;
      this.message.messageDisplayProperties.backgroundColor = this.selectedBackgroundColor;
    } else {
      this.selectedFontColor = this.message.messageDisplayProperties.fontColor;
      this.selectedBackgroundColor = this.message.messageDisplayProperties.backgroundColor;
      this.message.messageDisplayProperties.fontColor = null;
      this.message.messageDisplayProperties.backgroundColor = null;
    }
  }

  addLink(): void {
    this.message.content += '[Linkdescription](link' + this.linkCounter + ')';
    this.linkCounter += 1;
    this.message.links.push('');
  }

  trackById(index: number, element: any): number {
    return index;
  }

  removeLink(pos: number): void {
    this.message.links.splice(pos, 1);
    this.message.content = this.message.content.replace(new RegExp('\\[[^()\\[\\]]*]\\(link' + (pos + 1) + '\\)', 'i'), '');
    for (let i = pos + 1; i < this.message.links.length + 1; i++) {
      this.message.content = this.message.content.replace(new RegExp('\\[([^()\\[\\]]*?)]\\(link' + (i + 1) + '\\)', 'i'),
        '[\$1](link' + i + ')');
    }
    this.linkCounter -= 1;
  }

  fileSelect(event: any): void {
    this.loadFileAsBas64(event.target.files[0],
      result => this.message.attachment = result);
    this.message.content += '[img]';
  }

  removeAttachment(): void {
    this.message.content = this.message.content.replace('[img]', '');
    this.message.attachment = '';
  }

  logoSelect(event: any): void {
    this.loadFileAsBas64(event.target.files[0],
      result => this.message.logoAttachment = result);
  }

  /**
   * Loads a given file as base64 and passes it to the callback
   * @param file File to be loaded
   * @param callback function that gets called with the file content as base64
   */
  loadFileAsBas64(file: any, callback: (result: string) => void): void {
    const reader = new FileReader();
    reader.onload = ev => {
      const result = ev.target.result;
      if (result instanceof ArrayBuffer) {
        const imageDataToString = new Uint8Array(result).reduce((acc, val) => acc + String.fromCharCode(val), '');
        callback(window.btoa(imageDataToString));
      }
    };
    reader.readAsArrayBuffer(file);
  }

  removeLogo(): void {
    this.message.logoAttachment = '';
  }

  /**
   * Create a data url from the base64 image data.
   * This url can be set as the source for an image in html/css to display it.
   * @param imageData image data in base 64
   */
  getDataUrlFromImageByteArray(imageData: string): string {
    var svgPrefixIndex: number = window.atob(imageData).indexOf("<svg")
    var filetype = "png"
    if(svgPrefixIndex != -1){
      filetype="svg+xml"
      imageData.substring(svgPrefixIndex, imageData.length)
    }
    return 'data:image/' + filetype + ';base64,' + imageData;
  }

  locationDataHide(): void {
    if (this.message.locationData == null) {
      this.message.locationData = this.locationData;
    } else {
      this.message.locationData = null;
    }
  }

  setEndtimeFromExpirationOffset(): void {
    if (this.expirationOffsetType != null && this.expirationOffset != null) {
      const currentTime = new Date();
      const startTime = new Date(Date.parse(this.message.starttime));
      const referenceTime = (this.message.starttime != null && this.message.starttime.length !== 0)
        ? new Date(startTime.getTime() - startTime.getTimezoneOffset() * 60 * 1000)
        : new Date(currentTime.getTime() - currentTime.getTimezoneOffset() * 60 * 1000);
      const referenceTimeInMillis = referenceTime.getTime();
      let endTimeInMillis = null;
      switch (this.expirationOffsetType) {
        case OffsetType.Minute: {
          endTimeInMillis = referenceTimeInMillis + (this.expirationOffset * 60 * 1000);
          break;
        }
        case OffsetType.Hour: {
          endTimeInMillis = referenceTimeInMillis + (this.expirationOffset * 60 * 60 * 1000);
          break;
        }
        case OffsetType.Day: {
          endTimeInMillis = referenceTimeInMillis + (this.expirationOffset * 24 * 60 * 60 * 1000);
          break;
        }
        case OffsetType.Week: {
          endTimeInMillis = referenceTimeInMillis + (this.expirationOffset * 7 * 24 * 60 * 60 * 1000);
          break;
        }
        default: {
          // statements;
          break;
        }
      }
      this.message.endtime = new Date(endTimeInMillis).toISOString();
    } else {
      this.message.endtime = null;
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
        this.expirationOffsetType = OffsetType.Week;
        this.expirationOffset = millis / millisInAWeek;
      } else if (millis % millisInADay === 0) {
        this.expirationOffsetType = OffsetType.Day;
        this.expirationOffset = millis / millisInADay;
      } else if (millis % millisInAnHour === 0) {
        this.expirationOffsetType = OffsetType.Hour;
        this.expirationOffset = millis / millisInAnHour;
      } else if (millis % millisInAMinute === 0) {
        this.expirationOffsetType = OffsetType.Minute;
        this.expirationOffset = millis / millisInAMinute;
      }
    }
  }

  topicPropertySwitch($event: MatTabChangeEvent): void {
    if ($event.index === 0) {
      this.message.properties = [];
      this.message.topic = this.removedTopic;
    }
    if ($event.index === 1) {
      this.removedTopic = this.message.topic;
      this.message.topic = null;
      this.propertiesSelect();
    }
  }

  propertiesSelect(): void {
    this.message.properties = this.properties.filter(value => value[1]).map(value => value[0].binding);
  }
}
