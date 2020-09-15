import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Topic} from '../models/Topic';
import {environment} from '../../environments/environment';

@Component({
  selector: 'app-topic',
  templateUrl: './topic.component.html',
  styleUrls: ['./topic.component.css']
})
export class TopicComponent implements OnInit {

  constructor(private http: HttpClient) { }

  topic: Topic = {
    id: null,
    title: '',
    binding: '',
    tags: [],
    description: ''
  };

  ngOnInit(): void {
  }

  createTopic(): void {
    this.http.post(environment.backendApiPath + '/topic', this.topic, {}).subscribe(
      value => {
        console.log('topic was sent');
      },
      error => {
        console.log(error);
      }
    );
  }

  addTag(): void {
    this.topic.tags.push('');
  }

  trackById(index: number, element: any): number {
    return index;
  }

  removeTag(pos: number): void {
    this.topic.tags.splice(pos, 1);
  }

}
