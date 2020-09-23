import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Topic} from '../models/Topic';
import {Message} from '../models/Message';
import {Property} from '../models/Property';
import {LocationData} from '../models/LocationData';
import {environment} from '../../environments/environment';
import {fontFamilyToFontString} from '../models/FontFamily';

enum OffsetType {
  Minute, Hour, Day, Week
}

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.css']
})
export class MessageComponent implements OnInit {

  constructor(private http: HttpClient) {
  }

  topics: Topic[];

  properties: [Property, boolean][];

  message: Message = {
    topic: '',
    properties: [],
    content: '',
    sender: '',
    title: '',
    links: [],
    starttime: '',
    endtime: '',
    attachment: [],
    locationData: null,
  };

  fileName = 'Choose file';

  locationData: LocationData = {radius: 50};
  expirationOffset: number;
  expirationOffsetType: OffsetType = null;

  showTemplateList = true;

  hasTopicPropertiesError = false;
  hasSenderError = false;
  hasTitleError = false;
  hasContentError = false;
  coordValueRangeError = false;
  onlyOneCoordError = false;
  urlErrors: boolean[] = [];
  hasUrlErrors;

  fontFamilyToFontString = fontFamilyToFontString;

  sendMessage(): void {
    if (this.validateInputs()) {
      this.setEndtimeFromExpirationOffset();
      this.http.post(environment.backendApiPath + '/message', this.message, {}).subscribe(
        value => {
          console.log('send');
        },
        error => {
          console.log(error);
        }
      );
    }
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
    return !(this.hasTopicPropertiesError
      || this.hasSenderError
      || this.hasTitleError
      || this.hasContentError
      || this.coordValueRangeError
      || this.onlyOneCoordError
      || this.hasUrlErrors);
  }

  addLink(): void {
    this.message.links.push('');
  }

  trackById(index: number, element: any): number {
    return index;
  }

  removeLink(pos: number): void {
    this.message.links.splice(pos, 1);
  }

  fileSelect(event: any): void {
    const file = event.target.files[0];
    const reader = new FileReader();
    reader.onload = ev => {
      const result = ev.target.result;
      if (result instanceof ArrayBuffer) {
        this.message.attachment = [...(new Uint8Array(result))];
      }
    };
    reader.readAsArrayBuffer(file);
    this.fileName = file.name;
  }

  propertiesSelect(): void {
    this.message.properties = this.properties.filter(value => value[1]).map(value => value[0].binding);
  }

  locationDataHide(): void {
    if (this.message.locationData == null) {
      this.message.locationData = this.locationData;
    } else {
      this.message.locationData = null;
    }
  }

  toggleTemplateList(): void {
    this.showTemplateList = !this.showTemplateList;
  }

  loadTemplate($event: Message): void {
     this.message = $event;
     if (this.message.locationData != null) {
      this.locationData = $event.locationData;
     }
     this.properties = this.properties.map(property => [property[0], $event.properties.some(value => value === property[0].binding)]);
  }

  ngOnInit(): void {
    this.http.get(environment.backendApiPath + '/topic', {responseType: 'json'})
      .subscribe((topics: Topic[]) => this.topics = topics);
    this.http.get(environment.backendApiPath + '/property', {responseType: 'json'})
      .subscribe((properties: Property[]) => this.properties = properties.map(value => [value, false]));
  }

  clearMessage(): void {
    this.message = {
      topic: '',
      properties: [],
      content: '',
      sender: '',
      title: '',
      links: [],
      starttime: '',
      endtime: '',
      attachment: [],
      locationData: null,
    };
  }

  setEndtimeFromExpirationOffset(): void {
    if (this.expirationOffsetType != null && this.expirationOffset != null) {
      const currentTime = new Date();
      const referenceTime = (this.message.starttime != null && this.message.starttime.length !== 0)
        ? new Date(new Date(this.message.starttime).getTime() - currentTime.getTimezoneOffset() * 60 * 1000)
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
    } else{
      this.message.endtime = null;
    }
  }
}
