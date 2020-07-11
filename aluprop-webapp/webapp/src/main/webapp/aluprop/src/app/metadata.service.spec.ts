import { TestBed } from '@angular/core/testing';

import { MetadataService } from './metadata.service';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AppRoutingModule } from './app-routing.module';
import { MatDialogModule } from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';

describe('MetadataService', () => {
  let service: MetadataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        BrowserModule, 
        AppRoutingModule,
        MatDialogModule,
        TranslateModule.forRoot()
      ],
    });
    service = TestBed.inject(MetadataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
