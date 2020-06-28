import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InterestedUsersModalComponent } from './interested-users-modal.component';

describe('InterestedUsersModalComponent', () => {
  let component: InterestedUsersModalComponent;
  let fixture: ComponentFixture<InterestedUsersModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InterestedUsersModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InterestedUsersModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
