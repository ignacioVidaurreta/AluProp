import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InterestedUsersModalComponent } from './interested-users-modal.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';

describe('InterestedUsersModalComponent', () => {
  let component: InterestedUsersModalComponent;
  let fixture: ComponentFixture<InterestedUsersModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        BrowserModule, 
        AppRoutingModule,
        MatDialogModule,
      ],
      declarations: [ InterestedUsersModalComponent ],
      providers: [
        {
          provide: MatDialogRef,
          useValue: {}
        },
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InterestedUsersModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // TODO(Nachito): fix setup
  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
