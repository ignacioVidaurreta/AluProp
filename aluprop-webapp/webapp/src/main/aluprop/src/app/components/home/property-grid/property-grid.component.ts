import { Component, OnInit, Input, ViewChild, OnDestroy } from '@angular/core';
import { PropertyService } from 'src/app/services/property.service';
import { Property, SortOption } from 'src/app/models/property';
import { PageRequest } from 'src/app/interfaces/page-request';
import { Subscription } from 'rxjs';
import { PageResponse } from 'src/app/interfaces/page-response';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { ActivatedRoute, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { ImageService } from 'src/app/services/image.service';
import { DomSanitizer } from '@angular/platform-browser';
import {Neighborhood} from "../../../models/neighborhood";
import {Rule} from "../../../models/rule";
import {Service} from "../../../models/service";
import {TranslateService} from "@ngx-translate/core";
import {MetadataService} from "../../../metadata.service";

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

  neighborhoods: Neighborhood[];
  neighborhoodsSub: Subscription;
  rules: Rule[] = [];
  rulesSub: Subscription;
  services: Service[] = [];
  servicesSub: Subscription;

  languageChangedSub: Subscription;

  constructor(private propertyService: PropertyService,
              private route: ActivatedRoute,
              private router: Router,
              private imageService: ImageService,
              private _sanitizer: DomSanitizer,
              translateService: TranslateService,
              private metadataService: MetadataService) {
    this.languageChangedSub = translateService.onLangChange.subscribe((newLang) => this.translateRulesAndServices());
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
    if (this.rulesSub) { this.rulesSub.unsubscribe();}
    if (this.servicesSub) { this.servicesSub.unsubscribe();}
    if (this.neighborhoodsSub) { this.neighborhoodsSub.unsubscribe();}
  }

  onPageChange(pageEvent: PageEvent){
    this.pageRequest.pageNumber = pageEvent.pageIndex;
    this.pageRequest.pageSize = pageEvent.pageSize;
    this.propertiesSub.unsubscribe();
    this.createPageSubscription();
  }

  onSortSelected(event: any){
    this.router.navigate(['/'], { queryParams: {'orderBy': event.value}, queryParamsHandling: 'merge'});
  }

  createPageSubscription(){
    if (this.searchParams && Object.keys(this.searchParams).length !== 0){
      this.propertiesSub = this.propertyService.search(this.pageRequest, this.searchParams).subscribe((pageResponse) => {
        this.properties = pageResponse.responseData;
        this.totalItems = pageResponse.totalItems;
        this.pageSize = pageResponse.pageSize;
        this.fetchPropertyImages();
      });
      this.rulesSub = this.metadataService.getAllRules().subscribe((rules) => {
        this.rules = rules;
        this.translateRulesAndServices();
      });
      this.servicesSub = this.metadataService.getAllServices().subscribe((services) => {
        this.services = services;
        this.translateRulesAndServices();
      });
      this.neighborhoodsSub = this.metadataService.getAllNeighborhoods().subscribe((neighborhoods) => {
        this.neighborhoods = neighborhoods;
      });
    }
    else {
      this.propertiesSub = this.propertyService.getAll(this.pageRequest).subscribe((pageResponse) => {
        this.properties = pageResponse.responseData;
        this.totalItems = pageResponse.totalItems;
        this.pageSize = pageResponse.pageSize;
        this.fetchPropertyImages();
      });
    }
  }

  translateRulesAndServices(){
    this.metadataService.translateMetadataArray(this.rules);
    this.metadataService.translateMetadataArray(this.services);
  }

  fetchPropertyImages() {
    this.properties.forEach(
      (property) => {
        this.imageService.getImage(property.mainImage.id).subscribe(imageData => {
          property.mainImage.image = this._sanitizer.bypassSecurityTrustResourceUrl(imageData);
        });
      }
    );
  }

  getNeighborhoodName(neighborhoodId: number) {
    if(this.neighborhoods) {
      let index = this.neighborhoods.map((neighborhood) => {return neighborhood.id}).indexOf(neighborhoodId);
      return this.neighborhoods[index].name;
    }
  }

  getRuleNames(ruleIds: string[]) {
    if(this.rules) {
      let ruleNames = "";
      ruleIds.forEach((ruleId) => {
        let index = this.rules?.map((rule) => {return rule.id}).indexOf(+ruleId);
        ruleNames += this.rules[index]?.translatedText + ", ";
      })
      return ruleNames.slice(0, -2);
    }
  }

  getServiceNames(serivceIds: string[]) {
    if(this.services) {
      let ruleNames = "";
      serivceIds.forEach((serviceId) => {
        let index = this.services?.map((rule) => {return rule.id}).indexOf(+serviceId);
        ruleNames += this.services[index]?.translatedText + ", ";
      })
      return ruleNames.slice(0, -2);
    }
  }

}
