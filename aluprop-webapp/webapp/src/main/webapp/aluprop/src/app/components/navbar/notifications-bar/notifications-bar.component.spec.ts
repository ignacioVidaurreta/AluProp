import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NotificationsBarComponent } from './notifications-bar.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { MatDialogModule } from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';

describe('NotificationsBarComponent', () => {
  let component: NotificationsBarComponent;
  let fixture: ComponentFixture<NotificationsBarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        BrowserModule, 
        AppRoutingModule,
        MatDialogModule,
        TranslateModule.forRoot()
      ],
      declarations: [ NotificationsBarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotificationsBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
