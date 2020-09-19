import {Component, OnInit} from '@angular/core';
import {Message} from '../models/Message';
import {MessageHistory} from '../models/MessageHistory';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {HttpParams} from '@angular/common/http';
import {Topic} from '../models/Topic';
import {Property} from '../models/Property';

@Component({
  selector: 'app-message-history',
  templateUrl: './message-history.component.html',
  styleUrls: ['./message-history.component.css']
})
export class MessageHistoryComponent implements OnInit {

  constructor(private http: HttpClient) {
  }

  messageHistory: MessageHistory = {
    attachment: [],
    content: '',
    links: [],
    locationData: undefined,
    properties: [],
    sender: '',
    starttime: '',
    title: '',
    topic: '',
    starttime_period: '',
    endtime_period: ''
  };

  topics: Topic[];
  properties: [Property, boolean][];

  starttimePeriod = '';
  endtimePeriod = '';
  MessagesArray = [];
  hasTopicPropertiesError = false;

  ngOnInit(): void {
    this.http.get(environment.backendApiPath + '/topic', {responseType: 'json'})
      .subscribe((topics: Topic[]) => this.topics = topics);
    this.http.get(environment.backendApiPath + '/property', {responseType: 'json'})
      .subscribe((properties: Property[]) => this.properties = properties.map(value => [value, false]));
  }

  alreadySent(): boolean {
    const currentTime = new Date();
    const startTime = (this.messageHistory.starttime != null)
      ? new Date(new Date(this.messageHistory.starttime).getTime() - currentTime.getTimezoneOffset() * 60 * 1000)
      : new Date(currentTime.getTime() - currentTime.getTimezoneOffset() * 60 * 1000);

    return startTime < currentTime ? true : false;
  }

  showMessages(): void {
    console.log(this.starttimePeriod);
    console.log(this.endtimePeriod);
    // Add safe, URL encoded search parameter if there is a search term
    const options =
      {params: new HttpParams().set('starttimePeriod', this.starttimePeriod).set('endtimePeriod', this.endtimePeriod)};

    this.http.get<Message[]>(environment.backendApiPath + '/message', options)
      .subscribe(
        value => {
          this.MessagesArray = value;
          console.log(this.MessagesArray);
        },
        error => {
          console.log(error);
        }
      );
  }

}
