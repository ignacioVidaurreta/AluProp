import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailedPropertyComponent } from './detailed-property.component';

describe('DetailedPropertyComponent', () => {
  let component: DetailedPropertyComponent;
  let fixture: ComponentFixture<DetailedPropertyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
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
