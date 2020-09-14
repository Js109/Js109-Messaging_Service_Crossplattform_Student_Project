import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Property} from '../models/Property';

@Component({
  selector: 'app-property',
  templateUrl: './property.component.html',
  styleUrls: ['./property.component.css']
})
export class PropertyComponent implements OnInit {

  constructor(private http: HttpClient) { }

  property: Property = {
    id: null,
    name: '',
    binding: ''
  };

  createProperty(): void {
    this.http.post(environment.backendApiPath + '/property', this.property, {}).subscribe(
      value => {
        console.log('property was sent');
      },
      error => {
        console.log(error);
      }
    );
  }

  ngOnInit(): void {
  }
}
