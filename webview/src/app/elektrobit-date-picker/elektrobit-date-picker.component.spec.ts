import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ElektrobitDatePickerComponent } from './elektrobit-date-picker.component';

describe('ElektrobitDatePickerToDateComponent', () => {
  let component: ElektrobitDatePickerComponent;
  let fixture: ComponentFixture<ElektrobitDatePickerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ElektrobitDatePickerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ElektrobitDatePickerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
