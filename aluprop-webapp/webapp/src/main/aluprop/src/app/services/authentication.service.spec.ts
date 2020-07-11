import { TestBed } from '@angular/core/testing';

import { AuthenticationService } from './authentication.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from '../app-routing.module';

describe('AuthenticationService', () => {
  let service: AuthenticationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, BrowserModule, AppRoutingModule]
    });
    service = TestBed.inject(AuthenticationService);
  });

  // httpTestingController = TestBed.get(HttpTestingController);
  // service = TestBed.get(AuthenticationService);

  // afterEach(() => {
  //   httpTestingController.verify();
  // })

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
