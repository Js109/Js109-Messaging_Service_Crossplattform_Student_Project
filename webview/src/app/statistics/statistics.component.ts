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
    this.http.post(environment.backendApiPath + '/metrics', this.metricsFilter, {responseType: 'json'})
      .subscribe((metrics: Metrics) => {
        this.metrics = metrics;

        this.http.get(environment.backendApiPath + '/metrics/topicSubscriptionDistribution', {responseType: 'json'})
          .subscribe((distribution) => {
            this.topicDistribution = distribution;

            const keys = Object.keys(this.metrics.sentMessagesByDateTimeSpan);
            const keys2 = Object.keys(this.metrics.scheduledMessagesByDateTimeSpan);

            this.sentMessagesOptions = {
              xAxis: {
                type: 'time'
              },
              yAxis: {
                type: 'value'
              },
              series: [{
                type: 'bar',
                data: Object.values(this.metrics.sentMessagesByDateTimeSpan).map((v, i) => [keys[i], v])
              }, {
                type: 'bar',
                data: Object.values(this.metrics.scheduledMessagesByDateTimeSpan).map((v, i) => [keys2[i], v])
              }]
            };

            const keys3 = Object.keys(this.metrics.subscriberGainByDateTimeSpan);

            this.subscriberGainOptions = {
              xAxis: {
                type: 'time'
              },
              yAxis: {
                type: 'value'
              },
              series: [{
                type: 'bar',
                date: Object.values(this.metrics.subscriberGainByDateTimeSpan).map((v, i) => [keys3[i], v])
              }]
            };

            const hoursInDay = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23];

            this.messagesByTimeOfDayOptions = {
              xAxis: {
                type: 'category',
                data: hoursInDay
              },
              yAxis: {
                type: 'value'
              },
              series: [{
                type: 'line',
                data: hoursInDay.map(h => (this.metrics.sentMessagesByTimeOfDayTimeSpan.hasOwnProperty(h)) ? this.metrics.sentMessagesByTimeOfDayTimeSpan[h] / this.metrics.sentMessagesTotalGain : 0)
              }, {
                type: 'line',
                data: hoursInDay.map(h => (this.metrics.sentMessagesByTimeOfDayAllTime.hasOwnProperty(h)) ? this.metrics.sentMessagesByTimeOfDayAllTime[h] / this.metrics.sentMessagesTotalAllTime : 0)
              }]
            };

            const topics = Object.keys(this.topicDistribution);

            this.topicsPieOptions = {
              legend: {
                data: topics
              },
              series: [{
                type: 'pie',
                radius: [50, 100],
                center: ['25%', '50%'],
                roseType: 'area',
                data: topics.map(t => this.topicDistribution[t])
              }]
            };
          });
      });
  }

}
