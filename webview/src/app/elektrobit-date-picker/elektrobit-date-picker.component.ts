import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import {Moment} from 'moment';
import * as moment from 'moment';

@Component({
  selector: 'app-elektrobit-date-picker',
  templateUrl: './elektrobit-date-picker.component.html',
  styleUrls: ['./elektrobit-date-picker.component.css']
})
export class ElektrobitDatePickerComponent implements OnInit {
  constructor() { }

  @Input()
  date: string;

  @Output()
  dateChange = new EventEmitter<string>();

  @Input()
  invalid = false;

  minDateValue: Moment;
  @Input()
  set minDate(dateString: string) {
    this.minDateValue = moment(dateString, 'YYYY-MM-DD');
    if (this.currentMoment.isBefore(this.minDateValue)) {
      this.date =  this.minDateValue.local().format('YYYY-MM-DD');
      this.dateChange.emit(this.date);
    }
  }


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

  clearDate(): void {
    this.date = '';
    this.dateChange.emit(this.date);
  }

  /**
   * Function to transform messageFilter.starttimePeriod and endtimePeriod to a Moment object before binding it to the calendar.
   * To prevent constant rebuilding of the calendar the same instance of a Moment object needs to be returned
   * unless an actual change of messageFilter.starttimePeriod and endtimePeriod has occured.
   * If a new dateString is passed, a new Moment will be created from it and returned.
   */
  stringToMomentDate(dateString: string): Moment {
    if (this.currentDateString !== dateString || this.currentMoment == null) {
      this.currentDateString = dateString;
      this.currentMoment = moment(dateString, 'YYYY-MM-DD');
    }
    return this.currentMoment;
  }

  /**
   * Function that transforms the change event of the calendar into a string to bind it to messageFilter.startTimePeriod and endTimePeriod.
   * @param $event Change event containing the new date of the calendar as a Moment object.
   */
  momentToStringDate($event): void {
    this.date =  $event.value.local().format('YYYY-MM-DD');
    this.dateChange.emit(this.date);
  }

}
