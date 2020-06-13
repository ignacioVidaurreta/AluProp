import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProposalUsersTableComponent } from './proposal-users-table.component';

describe('ProposalUsersTableComponent', () => {
  let component: ProposalUsersTableComponent;
  let fixture: ComponentFixture<ProposalUsersTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProposalUsersTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProposalUsersTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

