import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InterestsTableComponent } from './interests-table.component';

describe('InterestsTableComponent', () => {
  let component: InterestsTableComponent;
  let fixture: ComponentFixture<InterestsTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InterestsTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InterestsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
