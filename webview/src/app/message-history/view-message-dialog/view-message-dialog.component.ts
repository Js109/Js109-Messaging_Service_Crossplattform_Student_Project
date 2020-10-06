import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-view-message-dialog',
  templateUrl: './view-message-dialog.component.html',
  styleUrls: ['./view-message-dialog.component.css']
})
export class ViewMessageDialogComponent implements OnInit {

  constructor() { }

  message;

  ngOnInit(): void {
  }

}
