import {Component, HostBinding, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {MetricsFilter} from '../models/MetricsFilter';
import {Topic} from '../models/Topic';
import {environment} from '../../environments/environment';
import {Property} from '../models/Property';
import {Metrics} from '../models/Metrics';

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {

  @HostBinding('class') class = 'flex-grow-1';

  constructor(private http: HttpClient) {
  }

  metricsFilter: MetricsFilter = {
    topicName: null,
    propertyName: null,
    timeSpanBegin: null,
    timeSpanEnd: null
  };

  topicDistribution: any;

  topics: Topic[];
  properties: Property[];
  metrics: Metrics = {};

  sentMessagesOptions: any;
  subscriberGainOptions: any;
  messagesByTimeOfDayOptions: any;
  topicsPieOptions: any;
  topicsBarOptions: any;

  topicCount = 0;

  showProportionChart = true;
  proportionTopicsCount = 5;

  hasMessageDates = true;
  hasSubscriberDates = true;

  ngOnInit(): void {
    this.http.get(environment.backendApiPath + '/topic', {responseType: 'json'})
      .subscribe((topics: Topic[]) => this.topics = topics);
    this.http.get(environment.backendApiPath + '/property', {responseType: 'json'})
      .subscribe((properties: Property[]) => this.properties = properties);
    this.loadMetrics();
  }

  loadMetrics(): void {
    this.http.post(environment.backendApiPath + '/metrics', this.metricsFilter, {responseType: 'json'})
      .subscribe((metrics: Metrics) => {
        this.metrics = metrics;

        this.setupMessageChart();

        this.setupSubscriberChart();

        this.setupTimeChart();
      });
    this.http.get(environment.backendApiPath + '/metrics/topicSubscriptionDistribution', {responseType: 'json'})
      .subscribe((distribution) => {
        this.topicDistribution = distribution;

        this.setupTopicCharts();
      });
  }

  /**
   * Creates a string array containing all dates between the start date and end date.
   * If the respective fields are set in metricsFilter those values will be used instead.
   * One day is added to the beginning and end of the range of dates.
   * @param startDate Date at which the array should start. Is ignored if metricsFilter.timeSpanBegin is set.
   * @param endDate Date at which the array should end. Is ignored if metricsFilter.timeSpanEnd is set.
   */
  private createListOfDates(startDate: number, endDate: number): string[] {
    if (this.metricsFilter.timeSpanBegin != null) {
      startDate = Date.parse(this.metricsFilter.timeSpanBegin);
    }
    if (this.metricsFilter.timeSpanEnd != null) {
      endDate = Date.parse(this.metricsFilter.timeSpanEnd);
    }

    const millisInADay = 1000 * 60 * 60 * 24;
    startDate = startDate - millisInADay;
    endDate = endDate + millisInADay;

    const dates = [];

    for (let i = startDate; i <= endDate; i = i + millisInADay) {
      dates.push(i);
    }

    return dates.map(v => {
      return new Date(v).toISOString().slice(0, 10);
    });
  }

  /**
   * Finds the smallest/largest date in the sentMessages and scheduledMessages array and calls createListOfDates with those values.
   */
  private createMessageDateList(sentMessages: [string, number][], scheduledMessages: [string, number][]): string[] {
    if (sentMessages.length === 0 && scheduledMessages.length === 0) {
      return [];
    }

    const sentDates = sentMessages
      .map(value => Date.parse(value[0]))
      .sort((a, b) => a - b);
    const scheduledDates = scheduledMessages
      .map(value => Date.parse(value[0]))
      .sort((a, b) => a - b);

    let startDate;
    let endDate;

    if (sentDates.length !== 0) {
      startDate = sentDates[0];
      endDate = sentDates[sentDates.length - 1];
    }

    if (scheduledDates.length !== 0) {
      if (scheduledDates[0] < startDate) {
        startDate = scheduledDates[0];
      }
      if (scheduledDates[scheduledDates.length - 1] > endDate) {
        endDate = scheduledDates[scheduledDates.length - 1];
      }
    }

    return this.createListOfDates(startDate, endDate);
  }

  setupMessageChart(): void {
    const sentMessages = Object.entries(this.metrics.sentMessagesByDateTimeSpan) as [string, number][];
    const scheduledMessages = Object.entries(this.metrics.scheduledMessagesByDateTimeSpan) as [string, number][];

    const dates = this.createMessageDateList(sentMessages, scheduledMessages);

    this.hasMessageDates = dates.length !== 0;

    this.sentMessagesOptions = {
      title: {
        text: 'Messages per Day'
      },
      xAxis: {
        type: 'category',
        data: dates,
        min: dates[0],
        max: dates[dates.length - 1]
      },
      yAxis: {
        type: 'value',
        minInterval: 1
      },
      color: ['#339933', '#3F3F3F', '#00c400', '#646464', '#00EB00', '#c8c8c8'],
      legend: {
        left: 'center',
        top: 'bottom',
        data: ['Sent messages', 'Scheduled messages']
      },
      tooltip: {
        trigger: 'item',
        formatter: '{c} Messages'
      },
      series: [{
        type: 'bar',
        barWidth: '90%',
        name: 'Sent messages',
        data: sentMessages
      }, {
        type: 'bar',
        barWidth: '90%',
        name: 'Scheduled messages',
        data: scheduledMessages
      }
      ]
    };
  }

  /**
   * Finds the smallest/largest date in subscribers and calls createListOfDates with those values.
   */
  private createSubscriberDateList(subscribers: [string, number][]): string[] {
    if (subscribers.length === 0) {
      return [];
    }

    const subscriberDates = subscribers
      .map(v => Date.parse(v[0]))
      .sort((a, b) => a - b);
    return this.createListOfDates(subscriberDates[0], subscriberDates[subscriberDates.length - 1]);
  }

  setupSubscriberChart(): void {
    const subscribers = Object.entries(this.metrics.subscriberGainByDateTimeSpan) as [string, number][];

    const dates = this.createSubscriberDateList(subscribers);

    this.hasSubscriberDates = dates.length !== 0;

    this.subscriberGainOptions = {
      title: {
        text: 'Subscribers per Day'
      },
      xAxis: {
        type: 'category',
        data: dates,
        min: dates[0],
        max: dates[dates.length - 1]
      },
      yAxis: {
        type: 'value',
        minInterval: 1
      },
      color: ['#339933', '#3F3F3F', '#00c400', '#646464', '#00EB00', '#c8c8c8'],
      tooltip: {
        trigger: 'item',
        formatter: '{c} Subscribers'
      },
      series: [{
        type: 'bar',
        barWidth: '90%',
        data: subscribers
      }]
    };
  }

  setupTimeChart(): void {
    const hoursInDay = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24];

    const series = [{
      type: 'line',
      name: 'Messages in selected timespan',
      data: hoursInDay.map(h =>
        [h,
          (this.metrics.sentMessagesByTimeOfDayTimeSpan.hasOwnProperty(h % 24))
            ? this.metrics.sentMessagesByTimeOfDayTimeSpan[h % 24] / this.metrics.sentMessagesTotalGain
            : 0]
      )
    }];

    if (this.metricsFilter.timeSpanBegin != null || this.metricsFilter.timeSpanEnd != null) {
      series.push(
        {
          type: 'line',
          name: 'Messages all time',
          data: hoursInDay.map(h =>
            [h,
              (this.metrics.sentMessagesByTimeOfDayAllTime.hasOwnProperty(h % 24))
                ? this.metrics.sentMessagesByTimeOfDayAllTime[h % 24] / this.metrics.sentMessagesTotalAllTime
                : 0]
          )
        }
      );
    }

    this.messagesByTimeOfDayOptions = {
      title: {
        text: 'Messages by Time of Day'
      },
      xAxis: {
        type: 'value',
        data: hoursInDay,
        max: 24,
        min: 0,
        axisLabel: {
          formatter: (value, index) => {
            return value + ':00';
          }
        }
      },
      yAxis: {
        type: 'value',
        axisLabel: {
          formatter: (value, index) => (value * 100) + '%'
        }
      },
      color: ['#339933', '#3F3F3F', '#00c400', '#646464', '#00EB00', '#c8c8c8'],
      legend: {
        left: 'center',
        top: 'bottom',
        data: ['Messages in selected timespan', 'Messages all time'],
        show: this.metricsFilter.timeSpanBegin != null || this.metricsFilter.timeSpanEnd != null
      },
      series
    };
  }

  setupTopicCharts(): void {
    const topics = Object.entries(this.topicDistribution).sort((a: [string, number], b: [string, number]) => b[1] - a[1]);

    this.topicCount = topics.length;
    const topicPicks = topics.slice(0, this.proportionTopicsCount);

    this.topicsPieOptions = {
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c} Subscriber'
      },
      color: ['#339933', '#3F3F3F', '#00c400', '#646464', '#00EB00', '#c8c8c8'],
      series: [{
        type: 'pie',
        radius: [20, 100],
        roseType: 'area',
        data: topicPicks.map(t => ({value: t[1], name: t[0]}))
      }]
    };

    this.topicsBarOptions = {
      xAxis: {
        type: 'value'
      },
      yAxis: {
        type: 'category',
        data: topics.map(v => v[0]),
        inverse: true
      },
      color: ['#339933', '#3F3F3F', '#00c400', '#646464', '#00EB00', '#c8c8c8'],
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c} Subscriber'
      },
      series: [{
        type: 'bar',
        barWidth: '90%',
        data: topics.map(v => ({name: v[0], value: v[1]}))
      }]
    };
  }
}
