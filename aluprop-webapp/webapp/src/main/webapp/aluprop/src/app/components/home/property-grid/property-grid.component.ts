import { Component, OnInit, Input, ViewChild, OnDestroy } from '@angular/core';
import { PropertyService } from 'src/app/services/property.service';
import { Property } from 'src/app/models/property';
import { PageRequest } from 'src/app/interfaces/page-request';
import { Subscription } from 'rxjs';
import { PageResponse } from 'src/app/interfaces/page-response';
import { MatPaginator, PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-property-grid',
  templateUrl: './property-grid.component.html',
  styleUrls: ['./property-grid.component.scss']
})
export class PropertyGridComponent implements OnInit, OnDestroy {

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  totalItems: number;
  pageSize: number;

  pageRequest: PageRequest;
  pageResponse: PageResponse<Property>;

  properties: Property[];
  propertiesSub: Subscription;

  constructor(private propertyService: PropertyService) { }

  ngOnInit(): void {
    this.pageRequest = {pageNumber: 0, pageSize: 12}
    this.createPageSubscription();
  }

  ngOnDestroy(): void {
    this.propertiesSub.unsubscribe();
  }

  onPageChange(pageEvent: PageEvent){
    this.pageRequest.pageNumber = pageEvent.pageIndex;
    this.pageRequest.pageSize = pageEvent.pageSize;
    this.propertiesSub.unsubscribe();
    this.createPageSubscription();
  }

  createPageSubscription(){
    this.propertiesSub = this.propertyService.getAll(this.pageRequest).subscribe((pageResponse) => {
      this.properties = pageResponse.responseData;
      console.log(pageResponse);
      this.totalItems = pageResponse.totalItems;
      this.pageSize = pageResponse.pageSize;
    });
  }

}