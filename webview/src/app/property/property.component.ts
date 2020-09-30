import {Component, HostBinding, OnInit} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Property} from '../models/Property';

@Component({
  selector: 'app-property',
  templateUrl: './property.component.html',
  styleUrls: ['./property.component.css'],
})
export class PropertyComponent implements OnInit {

  @HostBinding('class') class = 'flex-grow-1';

  constructor(private http: HttpClient) { }

  properties: Property[];

  property: Property = {
    id: null,
    name: '',
    binding: ''
  };

  createProperty(): void {
    this.http.post(environment.backendApiPath + '/property', this.property, {}).subscribe(
      value => {
        console.log('property was sent');
        this.loadProperties();
      },
      error => {
        console.log(error);
      }
    );
  }

  ngOnInit(): void {
    this.loadProperties();
  }

  loadProperties(): void {
    this.http.get(environment.backendApiPath + '/property?showDisabledProperties=true', {responseType: 'json'})
      .subscribe((properties: Property[]) => this.properties = properties);
  }

  updateProperty(property: Property): void {
    this.http.put(environment.backendApiPath + '/property/' + property.id, {disabled: !property.disabled})
      .subscribe(value => {
        console.log('Updated property ' + property.id);
        this.loadProperties();
      });
  }
}
