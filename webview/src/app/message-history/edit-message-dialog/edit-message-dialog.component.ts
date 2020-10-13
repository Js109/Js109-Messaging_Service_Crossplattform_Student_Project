import { Component, OnInit } from '@angular/core';
import {Message} from '../../models/Message';

@Component({
  selector: 'app-edit-message',
  templateUrl: './edit-message-dialog.component.html',
  styleUrls: ['./edit-message-dialog.component.css']
})
export class EditMessageDialogComponent implements OnInit {

  constructor() { }

  message;

  ngOnInit(): void {
  }

}
