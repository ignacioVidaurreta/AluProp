import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InterestsTableComponent } from './interests-table.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { BrowserModule } from '@angular/platform-browser';
import { MatDialogModule } from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';

describe('InterestsTableComponent', () => {
  let component: InterestsTableComponent;
  let fixture: ComponentFixture<InterestsTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        BrowserModule, 
        AppRoutingModule,
        MatDialogModule,
        TranslateModule.forRoot()
      ],
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
