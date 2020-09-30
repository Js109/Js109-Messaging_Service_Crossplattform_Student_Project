import {Component, HostBinding, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Topic} from '../models/Topic';
import {environment} from '../../environments/environment';
import {MatDialog} from '@angular/material/dialog';
import {TopicDescriptionDialogComponent} from './topic-description-dialog/topic-description-dialog.component';

@Component({
  selector: 'app-topic',
  templateUrl: './topic.component.html',
  styleUrls: ['./topic.component.css'],
})
export class TopicComponent implements OnInit {

  @HostBinding('class') class = 'flex-grow-1';

  constructor(private http: HttpClient, private dialog: MatDialog) { }

  topic: Topic = {
    id: null,
    title: '',
    binding: '',
    tags: [],
    description: ''
  };

  topics: Topic[];

  ngOnInit(): void {
    this.loadTopics();
  }

  loadTopics(): void {
    this.http.get(environment.backendApiPath + '/topic?showDisabledTopics=true', {responseType: 'json'})
      .subscribe((topics: Topic[]) => this.topics = topics);
  }

  createTopic(): void {
    this.http.post(environment.backendApiPath + '/topic', this.topic, {}).subscribe(
      value => {
        console.log('topic was sent');
        this.loadTopics();
      },
      error => {
        console.log(error);
      }
    );
  }

  addTag(): void {
    this.topic.tags.push('');
  }

  updateTopic(topic: Topic): void {
    this.http.put(environment.backendApiPath + '/topic/' + topic.id, {disabled: !topic.disabled})
      .subscribe(value => {
        console.log('Updated property ' + topic.id);
        this.loadTopics();
      });
  }

  trackById(index: number, element: any): number {
    return index;
  }

  removeTag(pos: number): void {
    this.topic.tags.splice(pos, 1);
  }

  editDescription(topic: Topic): void {
    const dialogRef = this.dialog.open(TopicDescriptionDialogComponent, {
      height: '50%',
      width: '50%'
    });

    dialogRef.componentInstance.topicName = topic.title;
    dialogRef.componentInstance.topicDescription = topic.description;

    dialogRef.afterClosed().subscribe(dialogResult => {
      this.http.patch(environment.backendApiPath + '/topic/' + topic.id, {description: dialogResult})
        .subscribe(
          value => {
            console.log('Successfully updated description');
            this.loadTopics();
          },
          error => console.log('Could not update description: ' + error)
        );
    });
  }
}
