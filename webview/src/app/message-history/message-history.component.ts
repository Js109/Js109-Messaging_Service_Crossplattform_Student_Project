import {Component, NgZone, OnInit} from '@angular/core';
import {Message} from '../models/Message';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {HttpParams} from '@angular/common/http';
import {Topic} from '../models/Topic';
import {Property} from '../models/Property';
import {MessageFilter} from '../models/MessageFilter';
import { NgbModalConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {MatDialog} from '@angular/material/dialog';
import {SaveTemplateDialogComponent} from '../message/template-load/save-template-dialog/save-template-dialog.component';
import {TemplateMessage} from '../models/TemplateMessage';
import {EditMessageDialogComponent} from './edit-message-dialog/edit-message-dialog.component';

@Component({
  selector: 'app-message-history',
  templateUrl: './message-history.component.html',
  // add NgbModalConfig and NgbModal to the component providers
  providers: [NgbModalConfig, NgbModal],
  styleUrls: ['./message-history.component.css']
})
export class MessageHistoryComponent implements OnInit {

  constructor(private http: HttpClient, private ngZone: NgZone, config: NgbModalConfig, private modalService: NgbModal, private dialog: MatDialog) {
    // customize default values of modals used by this component tree
    config.backdrop = 'static';
    config.keyboard = false;
  }

  messageFilter: MessageFilter = {
    searchString: '',
    starttimePeriod: '',
    endtimePeriod: '',
    topic: ''
  };

  topics: Topic[];
  properties: [Property, boolean][];
  messagesArray = [];
  hasDateRangeError = false;
  hasTopicPropertiesError = false;
  chosenMessage;

  ngOnInit(): void {
    this.http.get(environment.backendApiPath + '/topic', {responseType: 'json'})
      .subscribe((topics: Topic[]) => this.topics = topics);
    this.http.get(environment.backendApiPath + '/property', {responseType: 'json'})
      .subscribe((properties: Property[]) => this.properties = properties.map(value => [value, false]));
  }

  validateInputs(): boolean {
    this.hasDateRangeError = ((this.messageFilter.starttimePeriod === '' && this.messageFilter.endtimePeriod !== '') ||
      (this.messageFilter.starttimePeriod !== '' && this.messageFilter.endtimePeriod === '') ||
      new Date(this.messageFilter.starttimePeriod).getTime() > new Date(this.messageFilter.endtimePeriod).getTime());
    return !(this.hasDateRangeError);
  }

  showMessages(): void {
    if (this.validateInputs()) {
      // Add safe, URL encoded search parameter if there is a search term
      const options =
        {
          params: new HttpParams().set('searchString', this.messageFilter.searchString)
            .set('startTimePeriod', this.messageFilter.starttimePeriod)
            .set('endTimePeriod', this.messageFilter.endtimePeriod).set('topic', this.messageFilter.topic)
        };

      this.http.get<Message[]>(environment.backendApiPath + '/message', options)
        .subscribe(
          value => {
            this.messagesArray = value;
            console.log(this.messagesArray);
          },
          error => {
            console.log(error);
          }
        );
    }
  }

  deleteMessage(id: number): void {
    this.http.delete(environment.backendApiPath + '/message/' + id, ).subscribe(
      value => {
        console.log(`send delete message with ${id}`);
      },
      error => {
        console.log(error);
      },
      () => {
        this.ngZone.run( () => {
          this.showMessages();
        });
      }
    );
  }

  open(content, message): void {
    this.chosenMessage = message;
    this.modalService.open(content);
  }

  editMessage(message: Message): void {
    const dialogRef = this.dialog.open(EditMessageDialogComponent, {height: '80%', width: '60%'});

    dialogRef.componentInstance.message = message;
    dialogRef.afterClosed().subscribe(dialogResult => {
      if (dialogResult.action === 'save'){
        console.log('save' + dialogResult.message.title);
      } else if (dialogResult.action === 'copy'){
        console.log('copy' + dialogResult.message.title);
      }
    });
  }

}
