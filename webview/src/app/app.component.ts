import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Topic} from './models/Topic';
import {Message} from './models/Message';
import {Property} from './models/Property';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  constructor(private http: HttpClient) {
  }
  title = 'webview';

  topics: Topic[];

  properties: Property[];

  message: Message = {topic: '', content: '', sender: '', title: ''};

  sendMessage(): void {
    this.http.post('http://localhost:8080/message', this.message, {});
  }

  ngOnInit(): void {
    this.http.get('http://localhost:8080/topic', {responseType: 'json'})
      .subscribe((topics: Topic[]) => this.topics = topics);
    this.http.get('http://localhost:8080/property', {responseType: 'json'})
      .subscribe((properties: Property[]) => this.properties = properties);
  }
}
