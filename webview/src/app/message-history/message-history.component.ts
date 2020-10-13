import {Component, NgZone, OnInit} from '@angular/core';
import {Message} from '../models/Message';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {HttpParams} from '@angular/common/http';
import {Topic} from '../models/Topic';
import {Property} from '../models/Property';
import {MessageFilter} from '../models/MessageFilter';
import {NgbModalConfig, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {MatDialog} from '@angular/material/dialog';
import {EditMessageDialogComponent} from './edit-message-dialog/edit-message-dialog.component';
import {ViewMessageDialogComponent} from './view-message-dialog/view-message-dialog.component';
import {Router} from '@angular/router';
import {MatTabChangeEvent} from "@angular/material/tabs"; // import router from angular router

@Component({
  selector: 'app-message-history',
  templateUrl: './message-history.component.html',
  // add NgbModalConfig and NgbModal to the component providers
  providers: [NgbModalConfig, NgbModal],
  styleUrls: ['./message-history.component.css']
})
export class MessageHistoryComponent implements OnInit {

  constructor(private http: HttpClient, private ngZone: NgZone, config: NgbModalConfig,
              private modalService: NgbModal, private dialog: MatDialog, private route: Router) {
    // customize default values of modals used by this component tree
    config.backdrop = 'static';
    config.keyboard = false;
  }

  messageFilter: MessageFilter = {
    searchString: '',
    starttimePeriod: '',
    endtimePeriod: '',
    topic: '',
    sender: '',
    content: '',
    title: '',
    property: ''
  };

  topics: Topic[];
  removedTopic: string;
  properties: [Property, boolean][];
  removedProperty: string;
  messagesArray = [];
  hasDateRangeError = false;
  hasDatePickerOnlyOnceSelectedError = false;
  hasTopicPropertiesError = false;

  ngOnInit(): void {
    this.http.get(environment.backendApiPath + '/topic', {responseType: 'json'})
      .subscribe((topics: Topic[]) => this.topics = topics);
    this.http.get(environment.backendApiPath + '/property', {responseType: 'json'})
      .subscribe((properties: Property[]) => this.properties = properties.map(value => [value, false]));
  }

  validateInputs(): boolean {
    this.hasDatePickerOnlyOnceSelectedError = ((this.messageFilter.starttimePeriod === '' && this.messageFilter.endtimePeriod !== '') ||
      (this.messageFilter.starttimePeriod !== '' && this.messageFilter.endtimePeriod === ''));

    this.hasDateRangeError = new Date(this.messageFilter.starttimePeriod).getTime() > new Date(this.messageFilter.endtimePeriod).getTime();
    return !(this.hasDateRangeError && this.hasDatePickerOnlyOnceSelectedError);
  }

  showMessages(): void {
    if (this.validateInputs()) {
      // Add safe, URL encoded search parameter if there is a search term
      const options =
        {
          params: new HttpParams().set('searchString', this.messageFilter.searchString)
            .set('startTimePeriod', this.messageFilter.starttimePeriod)
            .set('endTimePeriod', this.messageFilter.endtimePeriod)
            .set('topic', this.messageFilter.topic)
            .set('content', this.messageFilter.content)
            .set('sender', this.messageFilter.sender)
            .set('title', this.messageFilter.title)
            .set('property', this.messageFilter.property)
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
    this.http.delete(environment.backendApiPath + '/message/' + id).subscribe(
      value => {
        console.log(`send delete message with ${id}`);
      },
      error => {
        console.log(error);
      },
      () => {
        this.ngZone.run(() => {
          this.showMessages();
        });
      }
    );
  }

  editMessage(message: Message): void {
    this.http.get<Message>(environment.backendApiPath + '/message/' + message.id)
      .subscribe(
        retrievedMessage => {
          const dialogRef = this.dialog.open(EditMessageDialogComponent, {height: '80%', width: '60%'});
          dialogRef.componentInstance.message = retrievedMessage;
          dialogRef.afterClosed().subscribe(dialogResult => {
            if (dialogResult.action === 'save') {
              this.http.put(environment.backendApiPath + '/message/' + dialogResult.message.id, dialogResult.message, {}).subscribe(
                value => {
                  console.log('sent put request to update message with given id');
                  this.showMessages();
                },
                error => {
                  console.log(error);
                }
              );
              console.log('save' + dialogResult.message.title);
            } else if (dialogResult.action === 'copy') {
              // Navigate to /message?messageId={retrievedMessage.id}
              this.route.navigate(['/message'], {queryParams: {messageId: retrievedMessage.id}});
            }
          });
        },
        error => {
          console.log(error);
        }
      );
  }

  viewMessage(message: Message): void {
    this.http.get<Message>(environment.backendApiPath + '/message/' + message.id)
      .subscribe(
        retrievedMessage => {
          const dialogRef = this.dialog.open(ViewMessageDialogComponent, {height: '55%', width: '50%'});
          dialogRef.componentInstance.message = retrievedMessage;
          dialogRef.afterClosed().subscribe(dialogResult => {
              if (dialogResult.action === 'copy') {
                // Navigate to /message?messageId={retrievedMessage.id}
                this.route.navigate(['/message'], {queryParams: {messageId: retrievedMessage.id}});
              }
            }
          );
        },
        error => {
          console.log(error);
        }
      );
  }

  topicPropertySwitch($event: MatTabChangeEvent): void {
    if ($event.index === 0) {
      this.removedProperty = this.messageFilter.property;
      this.messageFilter.property = null;
      this.messageFilter.topic = this.removedTopic;
    }
    if ($event.index === 1) {
      this.removedTopic = this.messageFilter.topic;
      this.messageFilter.topic = null;
      this.messageFilter.property =  this.removedProperty;
    }
  }
}
