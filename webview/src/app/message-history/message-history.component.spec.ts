import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MessageHistoryComponent } from './message-history.component';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('MessageHistoryComponent', () => {
  let component: MessageHistoryComponent;
  let fixture: ComponentFixture<MessageHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MessageHistoryComponent ],
      imports: [HttpClientTestingModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MessageHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
