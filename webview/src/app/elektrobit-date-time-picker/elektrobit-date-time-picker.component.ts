import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Moment} from 'moment';
import * as moment from 'moment';

@Component({
  selector: 'app-elektrobit-date-time-picker',
  templateUrl: './elektrobit-date-time-picker.component.html',
  styleUrls: ['./elektrobit-date-time-picker.component.css']
})
export class ElektrobitDateTimePickerComponent implements OnInit {

  constructor() { }

  @Input()
  date: string;

  @Output()
  dateChange = new EventEmitter<string>();

  @Input()
  invalid = false;

  currentDate = moment();

  /**
   * Field storing the current date in the date picker calendar to return it in the binding function
   * to prevent unnecessary rebuilding of the calendar.
   */
  currentMoment: Moment;
  /**
   * Field for storing the string the current date in the calendar is based on
   * to detect actual changes in the date.
   */
  currentDateString: string;

  ngOnInit(): void {
  }

  /**
   * Function to transform message.starttime to a Moment object before binding it to the calendar.
   * To prevent constant rebuilding of the calendar the same instance of a Moment object needs to be returned
   * unless an actual change of message.starttime has occured.
   * If a new dateString is passed (that is if message.starttime was changed) a new Moment will be created from it and returned.
   */
  stringToMoment(dateString: string): Moment {
    if (this.currentDateString !== dateString || this.currentMoment == null) {
      this.currentDateString = dateString;
      this.currentMoment = moment(dateString, 'YYYY-MM-DD[T]HH:mm:ss');
    }
    return this.currentMoment;
  }

  clearDate(): void {
    this.date = null;
    this.dateChange.emit(this.date);
  }

  /**
   * Function that transforms the change event of the calendar into a string to bind it to message.starttime.
   * @param $event Change event containing the new date of the calendar as a Moment object.
   */
  momentToString($event): void {
    this.date =  $event.value.local().format('YYYY-MM-DD[T]HH:mm:ss');
    this.dateChange.emit(this.date);
  }
}
