import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProposalUsersTableComponent } from './proposal-users-table.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { MatDialogModule } from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';

describe('ProposalUsersTableComponent', () => {
  let component: ProposalUsersTableComponent;
  let fixture: ComponentFixture<ProposalUsersTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        BrowserModule, 
        AppRoutingModule,
        MatDialogModule,
        TranslateModule.forRoot()
      ],
      declarations: [ ProposalUsersTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProposalUsersTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // TODO(Nachito) fix setup. https://stackoverflow.com/questions/40142300/cannot-read-property-subscribe-of-undefined-after-running-npm-test-angular-2
  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});

