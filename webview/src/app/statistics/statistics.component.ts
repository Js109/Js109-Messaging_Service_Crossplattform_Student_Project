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

  constructor(private http: HttpClient) { }

  metricsFilter: MetricsFilter = {
    topicName: null,
    propertyName: null,
    timeSpanBegin: null,
    timeSpanEnd: null
  };

  topics: Topic[];
  properties: Property[];
  metrics: Metrics;

  diagramOptions: any;

  ngOnInit(): void {
    this.http.get(environment.backendApiPath + '/topic', {responseType: 'json'})
      .subscribe((topics: Topic[]) => this.topics = topics);
    this.http.get(environment.backendApiPath + '/property', {responseType: 'json'})
      .subscribe((properties: Property[]) => this.properties = properties);
    this.http.post(environment.backendApiPath + '/metrics', this.metricsFilter, {responseType: 'json'})
      .subscribe((metrics: Metrics) => {
        this.metrics = metrics;

        this.diagramOptions = {
          xAxis: {
            type: 'category',
            data: Object.keys(this.metrics.sentMessagesByDateTimeSpan)
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            type: 'line',
            data: Object.values(this.metrics.sentMessagesByDateTimeSpan)
          }]
        };
        console.log('test');
      });
  }

}
