import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-save-template-dialog',
  templateUrl: './save-template-dialog.component.html',
  styleUrls: ['./save-template-dialog.component.css']
})
export class SaveTemplateDialogComponent implements OnInit {

  constructor() { }

  templateName: string;

  ngOnInit(): void {
  }

}
