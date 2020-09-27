import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Message} from '../models/Message';
import {environment} from '../../environments/environment';


@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.css']
})
export class MessageComponent implements OnInit {

  constructor(private http: HttpClient) {
  }

  showTemplateList = true;

  message: Message = {
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
  };

  sendMessage(message: Message): void {
    this.http.post(environment.backendApiPath + '/message', message, {}).subscribe(
      value => {
        console.log('send');
      },
      error => {
        console.log(error);
      }
    );
  }

  toggleTemplateList(): void {
    this.showTemplateList = !this.showTemplateList;
  }

  loadTemplate($event: Message): void {
     this.message = $event;
  }

  ngOnInit(): void {
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
      attachment: '',
      locationData: null,
      logoAttachment: ''
    };
  }
}
