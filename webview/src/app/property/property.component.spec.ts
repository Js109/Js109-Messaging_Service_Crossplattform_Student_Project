import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { PropertyComponent } from './property.component';
import {HttpClientModule} from '@angular/common/http';

describe('PropertyComponent', () => {
  let component: PropertyComponent;
  let fixture: ComponentFixture<PropertyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PropertyComponent ],
      imports: [HttpClientModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PropertyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
