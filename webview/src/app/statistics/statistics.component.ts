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
  metrics: Metrics;

  sentMessagesOptions: any;
  subscriberGainOptions: any;
  messagesByTimeOfDayOptions: any;
  topicsPieOptions: any;
  topicsBarOptions: any;

  showTop5 = true;

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

  metricsSpanBeginToMillis(value): number {
    let min = value.min;
    if (this.metricsFilter.timeSpanBegin != null) {
      min = Date.parse(this.metricsFilter.timeSpanBegin);
    }
    return min - 24 * 60 * 60 * 1000;
  }

  metricsSpanEndToMillis(value): number {
    let max = value.max;
    if (this.metricsFilter.timeSpanEnd != null) {
      max = Date.parse(this.metricsFilter.timeSpanEnd);
    }
    return max + 24 * 60 * 60 * 1000;
  }

  setupMessageChart(): void {
    const sentMessages = Object.entries(this.metrics.sentMessagesByDateTimeSpan);
    const scheduledMessages = Object.entries(this.metrics.scheduledMessagesByDateTimeSpan);

    this.sentMessagesOptions = {
      title: {
        text: 'Messages per Day'
      },
      xAxis: {
        type: 'time',
        min: (value) => {
          return this.metricsSpanBeginToMillis(value);
        },
        max: (value) => {
          return this.metricsSpanEndToMillis(value);
        },
        axisLabel: {
          formatter: (value, index) => {
            const date = new Date(value);
            return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
          }
        }
      },
      yAxis: {
        type: 'value'
      },
      color: ['#339933', '#3F3F3F', '#00c400', '#646464', '#00EB00', '#c8c8c8'],
      legend: {
        left: 'center',
        top: 'bottom',
        data: ['Sent messages', 'Scheduled messages']
      },
      tooltip: {
        trigger: 'item',
        formatter: '{b}'
      },
      series: [{
        type: 'bar',
        barWidth: '90%',
        name: 'Sent messages',
        data: sentMessages.map(v => ({value: v, name: v[0]}))
      }, {
        type: 'bar',
        barWidth: '90%',
        name: 'Scheduled messages',
        data: scheduledMessages.map(v => ({value: v, name: v[0]}))
      }]
    };
  }

  setupSubscriberChart(): void {
    const subscribers = Object.entries(this.metrics.subscriberGainByDateTimeSpan);

    this.subscriberGainOptions = {
      title: {
        text: 'Subscribers per Day'
      },
      xAxis: {
        type: 'time',
          min: (value) => {
          return this.metricsSpanBeginToMillis(value);
        },
          max: (value) => {
          return this.metricsSpanEndToMillis(value);
        },
          axisLabel: {
          formatter: (value, index) => {
            const date = new Date(value);
            return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
          }
        }
      },
      yAxis: {
        type: 'value'
      },
      color: ['#339933', '#3F3F3F', '#00c400', '#646464', '#00EB00', '#c8c8c8'],
        legend: {
      left: 'center',
        top: 'bottom',
        data: ['Sent messages', 'Scheduled messages']
    },
      tooltip: {
        trigger: 'item',
          formatter: '{b}'
      },
      series: [{
        type: 'bar',
        barWidth: '90%',
        data: subscribers.map(v => ({value: v, name: v[0]}))
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
    const topics = Object.entries(this.topicDistribution).sort((a: [string, number], b: [string, number]) => a[1] - b[1]);

    const top5 = topics.slice(0, 5);

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
        data: top5.map(t => ({value: t[1], name: t[0]}))
      }]
    };

    this.topicsBarOptions = {
      xAxis: {
        type: 'value'
      },
      yAxis: {
        type: 'category',
        data: topics.map(v => v[0])
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
