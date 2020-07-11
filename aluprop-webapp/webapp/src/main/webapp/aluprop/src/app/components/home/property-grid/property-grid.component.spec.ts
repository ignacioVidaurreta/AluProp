import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PropertyGridComponent } from './property-grid.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { MatDialogModule } from '@angular/material/dialog';

describe('PropertyGridComponent', () => {
  let component: PropertyGridComponent;
  let fixture: ComponentFixture<PropertyGridComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        BrowserModule,
        AppRoutingModule,
        MatDialogModule,
      ],
      declarations: [ PropertyGridComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PropertyGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
