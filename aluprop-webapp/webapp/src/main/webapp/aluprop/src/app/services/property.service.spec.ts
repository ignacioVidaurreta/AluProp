import { TestBed } from '@angular/core/testing';

import { PropertyService } from './property.service';
import { HttpClientTestingModule,
  HttpTestingController } from '@angular/common/http/testing';
import { PageRequest } from '../interfaces/page-request';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from '../app-routing.module';

describe('PropertyService', () => {
  let service: PropertyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, BrowserModule, AppRoutingModule]
    });
    service = TestBed.get(PropertyService);
  });


  // httpTestingController = TestBed.get(HttpTestingController);
  // service = TestBed.get(PropertyService);

  // afterEach(() => {
  //   httpTestingController.verify();
  // })

  // it('should be created', () => {
  //   expect(service).toBeTruthy();
  // });

  // it('should get all properties in a paginated way', () => {
  //   const mockPageRequest: PageRequest = {
  //     pageSize: 12,
  //     pageNumber: 1 
  //   }
  //   service.getAll(mockPageRequest)
  //     .subscribe(response => {
  //       expect(response.totalItems).toBeGreaterThan(0);
  //       expect(response.totalItems).toBeLessThan(mockPageRequest.pageSize);
  //     })
  // })
});
