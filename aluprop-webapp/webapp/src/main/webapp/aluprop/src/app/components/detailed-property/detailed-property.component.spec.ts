import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailedPropertyComponent } from './detailed-property.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { TranslateModule } from '@ngx-translate/core';
import { MatDialogModule } from '@angular/material/dialog';

describe('DetailedPropertyComponent', () => {
  let component: DetailedPropertyComponent;
  let fixture: ComponentFixture<DetailedPropertyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, BrowserModule, AppRoutingModule, TranslateModule.forRoot(), MatDialogModule],
      declarations: [ DetailedPropertyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailedPropertyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
