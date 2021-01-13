import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicDescriptionDialogComponent } from './topic-description-dialog.component';

describe('TopicDescriptionDialogComponent', () => {
  let component: TopicDescriptionDialogComponent;
  let fixture: ComponentFixture<TopicDescriptionDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TopicDescriptionDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TopicDescriptionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
