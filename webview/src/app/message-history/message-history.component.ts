import {Component, OnInit} from '@angular/core';
import {Message} from '../models/Message';
import {MessageHistory} from '../models/MessageHistory';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {HttpParams} from '@angular/common/http';

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

  starttimePeriod = '';
  endtimePeriod = '';
  MessagesArray = [];

  ngOnInit(): void {
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
          console.log('send');
        },
        error => {
          console.log(error);
        }
      );
  }

}
