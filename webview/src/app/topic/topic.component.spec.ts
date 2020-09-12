import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TopicComponent } from './topic.component';
import {HttpClientModule} from '@angular/common/http';

describe('TopicComponent', () => {
  let component: TopicComponent;
  let fixture: ComponentFixture<TopicComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TopicComponent ],
      imports: [HttpClientModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TopicComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
