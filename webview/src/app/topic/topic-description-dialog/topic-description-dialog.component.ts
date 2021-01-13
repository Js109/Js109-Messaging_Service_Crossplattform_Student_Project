import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-topic-description-dialog',
  templateUrl: './topic-description-dialog.component.html',
  styleUrls: ['./topic-description-dialog.component.css']
})
export class TopicDescriptionDialogComponent implements OnInit {

  topicName: string;

  topicDescription: string;

  constructor() { }

  ngOnInit(): void {
  }
}
