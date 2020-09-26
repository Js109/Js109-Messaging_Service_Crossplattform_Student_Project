import {Component, OnInit} from '@angular/core';
import {Message} from '../models/Message';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {HttpParams} from '@angular/common/http';
import {Topic} from '../models/Topic';
import {Property} from '../models/Property';
import {MessageHistory} from '../models/MessageHistory';

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
    endtime: '',
    isSent: false,
    title: '',
    topic: '',
    starttime_period: '',
    endtime_period: ''
  };

  topics: Topic[];
  properties: [Property, boolean][];

  searchString = '';
  starttimePeriod = '';
  endtimePeriod = '';
  MessagesArray = [];
  hasDateRangeError = false;
  hasTopicPropertiesError = false;

  ngOnInit(): void {
    this.http.get(environment.backendApiPath + '/topic', {responseType: 'json'})
      .subscribe((topics: Topic[]) => this.topics = topics);
    this.http.get(environment.backendApiPath + '/property', {responseType: 'json'})
      .subscribe((properties: Property[]) => this.properties = properties.map(value => [value, false]));
  }

  validateInputs(): boolean {
    this.hasDateRangeError = ((this.starttimePeriod === '' && this.endtimePeriod !== '') ||
      (this.starttimePeriod !== '' && this.endtimePeriod === '') ||
      new Date(this.starttimePeriod).getTime() > new Date(this.endtimePeriod).getTime());
    return !(this.hasDateRangeError);
  }

  showMessages(): void {
    if (this.validateInputs()) {
      console.log(this.starttimePeriod);
      console.log(this.endtimePeriod);
      // Add safe, URL encoded search parameter if there is a search term
      const options =
        {
          params: new HttpParams().set('searchString', this.searchString).set('startTimePeriod', this.starttimePeriod)
            .set('endTimePeriod', this.endtimePeriod).set('topic', this.messageHistory.topic)
        };

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

}
