import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TemplateLoadComponent } from './template-load.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {MatDialogModule} from '@angular/material/dialog';

describe('TemplateLoadComponent', () => {
  let component: TemplateLoadComponent;
  let fixture: ComponentFixture<TemplateLoadComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TemplateLoadComponent ],
      imports: [HttpClientTestingModule, MatDialogModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TemplateLoadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
