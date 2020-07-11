import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateProposalModalComponent } from './create-proposal-modal.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { TranslateModule } from '@ngx-translate/core';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { PropertyItemComponent } from '../../home/property-grid/property-item/property-item.component';
import { User } from 'src/app/models/user';
import { Property } from 'src/app/models/property';
import { MatSelectionList } from '@angular/material/list';

describe('CreateProposalModalComponent', () => {
  let component: CreateProposalModalComponent;
  let fixture: ComponentFixture<CreateProposalModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule, 
        BrowserModule, 
        AppRoutingModule, 
        TranslateModule.forRoot(), 
        MatDialogModule,

      ],
      declarations: [ CreateProposalModalComponent, PropertyItemComponent ],
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
    fixture = TestBed.createComponent(CreateProposalModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // TODO(Nachito): Fix setup
  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
