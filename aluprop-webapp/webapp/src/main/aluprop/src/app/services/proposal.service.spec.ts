import { TestBed } from '@angular/core/testing';

import { ProposalService } from './proposal.service';
import { AppRoutingModule } from '../app-routing.module';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('ProposalService', () => {
  let service: ProposalService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, BrowserModule, AppRoutingModule]
    });
    service = TestBed.inject(ProposalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
