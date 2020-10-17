import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ElektrobitDateTimePickerComponent } from './elektrobit-date-time-picker.component';

describe('ElektrobitDatePickerComponent', () => {
  let component: ElektrobitDateTimePickerComponent;
  let fixture: ComponentFixture<ElektrobitDateTimePickerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ElektrobitDateTimePickerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElektrobitDateTimePickerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
