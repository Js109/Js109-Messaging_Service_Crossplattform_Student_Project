import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Topic} from '../models/Topic';
import {Message} from '../models/Message';
import {Property} from '../models/Property';
import {LocationData} from '../models/LocationData';

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
    attachment: [],
    locationData: null
  };

  fileName = 'Choose file';

  locationData: LocationData = {radius: 50};

  sendMessage(): void {
    this.http.post('http://localhost:8080/message', this.message, {}).subscribe(
      value => {
        console.log('send');
      },
      error => {
        console.log(error);
      }
    );
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

  ngOnInit(): void {
    this.http.get('http://localhost:8080/topic', {responseType: 'json'})
      .subscribe((topics: Topic[]) => this.topics = topics);
    this.http.get('http://localhost:8080/property', {responseType: 'json'})
      .subscribe((properties: Property[]) => this.properties = properties.map(value => [value, false]));
  }
}
