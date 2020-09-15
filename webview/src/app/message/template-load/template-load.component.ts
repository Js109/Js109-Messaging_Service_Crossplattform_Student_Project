import {Component, OnInit, Output, EventEmitter, Input} from '@angular/core';
import {Message} from '../../models/Message';
import {HttpClient} from '@angular/common/http';
import {TemplateMessage} from '../../models/TemplateMessage';
import {environment} from '../../../environments/environment';
import {MatDialog} from '@angular/material/dialog';
import {SaveTemplateDialogComponent} from './save-template-dialog/save-template-dialog.component';

@Component({
  selector: 'app-template-load',
  templateUrl: './template-load.component.html',
  styleUrls: ['./template-load.component.css']
})
export class TemplateLoadComponent implements OnInit {
  @Output() templateSelected = new EventEmitter<Message>();

  @Input()
  currentMessage: Message;

  templates: TemplateMessage[];

  // needs to be an array as binding to a mat-selection-list always returns an array even when multiple=false
  selectedTemplate: TemplateMessage[];

  constructor(private http: HttpClient, private dialog: MatDialog) {
  }

  selectTemplate(): void {
    this.templateSelected.emit(this.selectedTemplate[0]);
    this.loadTemplates();
  }

  ngOnInit(): void {
    this.loadTemplates();
  }

  loadTemplates(): void {
    this.http.get(environment.backendApiPath + '/template').subscribe(
      (templates: TemplateMessage[]) => this.templates = templates
    );
    this.selectedTemplate = null;
  }

  deleteTemplate(): void {
    const templateId = this.selectedTemplate[0].id;
    this.http.delete(environment.backendApiPath + '/template/' + templateId)
      .subscribe(
        value => {
          console.log('Successfully deleted template ' + templateId);
          this.loadTemplates();
        },
        error => console.log('Could not delete template ' + templateId + ': ' + error)
      );
  }

  saveTemplate(): void {
    const dialogRef = this.dialog.open(SaveTemplateDialogComponent);

    dialogRef.afterClosed().subscribe(dialogResult => {
      const template = this.currentMessage as TemplateMessage;
      template.id = null;
      template.templateName = dialogResult;
      this.http.post(environment.backendApiPath + '/template', this.currentMessage)
        .subscribe(
          value => {
            console.log('Successfully stored template');
            this.loadTemplates();
          },
          error => console.log('Could not store template: ' + error)
        );
    });
  }
}
