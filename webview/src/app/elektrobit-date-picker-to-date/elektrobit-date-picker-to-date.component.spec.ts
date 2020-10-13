import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ElektrobitDatePickerToDateComponent } from './elektrobit-date-picker-to-date.component';

describe('ElektrobitDatePickerToDateComponent', () => {
  let component: ElektrobitDatePickerToDateComponent;
  let fixture: ComponentFixture<ElektrobitDatePickerToDateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ElektrobitDatePickerToDateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElektrobitDatePickerToDateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
