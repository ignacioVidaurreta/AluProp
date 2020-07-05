import { Component, OnInit, Input, ViewChild, OnDestroy } from '@angular/core';
import { PropertyService } from 'src/app/services/property.service';
import { Property, SortOption } from 'src/app/models/property';
import { PageRequest } from 'src/app/interfaces/page-request';
import { Subscription } from 'rxjs';
import { PageResponse } from 'src/app/interfaces/page-response';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { ActivatedRoute } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-property-grid',
  templateUrl: './property-grid.component.html',
  styleUrls: ['./property-grid.component.scss']
})
export class PropertyGridComponent implements OnInit, OnDestroy {

  sortOptions = [
    {
        text: 'home.sort.newest',
        value: SortOption.Newest
    },
    {
      text: 'home.sort.capacity-highest',
      value: SortOption.HighestCapacity
    },
    {
      text: 'home.sort.capacity-lowest',
      value: SortOption.LowestCapacity
    },
    {
      text: 'home.sort.price-highest',
      value: SortOption.HighestPrice
    },
    {
      text: 'home.sort.price-lowest',
      value: SortOption.LowestPrice
    },
    {
      text: 'home.sort.budget-highest',
      value: SortOption.HighestBudget
    },
    {
      text: 'home.sort.budget-lowest',
      value: SortOption.LowestBudget
    }];

  searchParamsSub: Subscription;
  searchParams: any;

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  totalItems: number;
  pageSize: number;

  pageRequest: PageRequest;
  pageResponse: PageResponse<Property>;

  properties: Property[];
  propertiesSub: Subscription;

  constructor(private propertyService: PropertyService,
              private route: ActivatedRoute) {
    this.pageRequest = {pageNumber: 0, pageSize: 12}

    this.searchParamsSub = route.queryParams.pipe(
      filter((params) => Object.keys(params).length !== 0)
    ).subscribe((params)=>{
      this.searchParams = params;
      this.createPageSubscription();
    });
  }

  ngOnInit(): void {
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
    if (this.searchParams && Object.keys(this.searchParams).length !== 0){
      this.propertiesSub = this.propertyService.search(this.pageRequest, this.searchParams).subscribe((pageResponse) => {
        this.properties = pageResponse.responseData;
        this.totalItems = pageResponse.totalItems;
        this.pageSize = pageResponse.pageSize;
        console.log(this.properties);
      });
    }
    else {
      this.propertiesSub = this.propertyService.getAll(this.pageRequest).subscribe((pageResponse) => {
        this.properties = pageResponse.responseData;
        this.totalItems = pageResponse.totalItems;
        this.pageSize = pageResponse.pageSize;
        console.log(this.properties);
      });
    }
    
  }

}