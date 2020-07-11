import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PropertyItemComponent } from './property-item.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { MatDialogModule } from '@angular/material/dialog';
import { PropertyGridComponent } from '../property-grid.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';

describe('PropertyGridComponent', () => {
  let component: PropertyItemComponent;
  let fixture: ComponentFixture<PropertyItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        BrowserModule, 
        AppRoutingModule,
        MatDialogModule,
      ],
      declarations: [ PropertyItemComponent,PropertyGridComponent],
      schemas: [NO_ERRORS_SCHEMA]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PropertyItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // TODO(nachito) Fix setup
  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
