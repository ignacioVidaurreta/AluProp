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

  invalidParams: boolean;

  constructor(private propertyService: PropertyService,
              private route: ActivatedRoute,
              private router: Router,
              private imageService: ImageService,
              private _sanitizer: DomSanitizer,
              translateService: TranslateService,
              private metadataService: MetadataService) {
    this.languageChangedSub = translateService.onLangChange.subscribe((newLang) => this.translateRulesAndServices());
    this.invalidParams = false;
    this.pageRequest = {pageNumber: 0, pageSize: 12}
    this.searchParamsSub = route.queryParams.pipe(
      filter((params) => Object.keys(params).length !== 0)
    ).subscribe((params)=>{
      if(this.urlParamsAreValid(params))
        this.searchParams = params;
      else {
        this.invalidParams = true;
      }
      this.createPageSubscription();
    });
  }

  ngOnInit(): void {
    this.createPageSubscription();
  }

  servicesAreValid(services: any): boolean {
    if(!(services instanceof Array) && !((typeof services) == "string")){
      return false;
    }
    let isValid: boolean = true;
    if(services instanceof Array) {
      services.forEach((serviceId) => {
        if(Number.isNaN(+serviceId) || (+serviceId) < 1 || (+serviceId) > 3){
          isValid = false;
          return isValid;
        }
      })
    }else{ // else is instanceof string
      if(Number.isNaN(+services) || (+services) < 1 || (+services) > 3)
      isValid = false;
    }
    
    return isValid;
  }

  rulesAreValid(rules: any): boolean {
    if(!(rules instanceof Array) && !((typeof rules) == "string")){
      return false;
    }
    let isValid: boolean = true;
    if(rules instanceof Array) {
      rules.forEach((ruleId) => {
        if(Number.isNaN(+ruleId) || (+ruleId) < 1 || (+ruleId) > 3)
          isValid = false;
          return isValid;
      })
    }else{ // else is instanceof string
      if(Number.isNaN(+rules) ||  (+rules) < 1 || (+rules) > 2)
        isValid = false;
    }
    return isValid;    
  }

  rangeIsValid(min: any, max: any): boolean{
    if(Number.isNaN(+min) || Number.isNaN(+max))
      return false;
    
    if(+min <0 || +max < 0 || +min > +max)
      return false

    return true;
  }



  valueIsInRange(value:any, min:number, max:number){
    if(Number.isNaN(+value))
      return false;

    return +value >= min && +value <= max;
  }
  urlParamsAreValid(params: any): boolean{
    let services = params.services || [];
    if(!this.servicesAreValid(services))
      return false;

    let rules = params.rules || [];
    if(!this.rulesAreValid(rules))
      return false;

    let minPrice = params.minPrice || 0;
    let maxPrice = params.maxPrice || 0;
    if(!this.rangeIsValid(minPrice, maxPrice))
      return false;
    
    let minCapacity = params.minCapacity || 0;
    let maxCapacity = params.maxCapacity || 0;
    if(!this.rangeIsValid(minCapacity, maxCapacity))
      return false;
    
    let privacy = params.privacy || 0;
    if(!this.valueIsInRange(privacy, 0, 1))
      return false;
    
    let propertyType = params.propertyType || 0;
    if(!this.valueIsInRange(propertyType, 0, 2))
      return false;
    
    let neighborhoodId = params.neighbourhoodId || 1;
    if(!this.valueIsInRange(neighborhoodId, 1, 5))
      return false;

    return true;
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

  getRuleNames(ruleIds: Array<string> | string) {
    if(this.rules) {
      let ruleNames = "";
      if(ruleIds instanceof Array) {
  
        ruleIds.forEach((ruleId) => {
          let index = this.rules?.map((rule) => {return rule.id}).indexOf(+ruleId);
          ruleNames += this.rules[index]?.translatedText + ", ";
        })
        return ruleNames.slice(0, -2);
      }
      else {
        let index = this.rules?.map((rule) => {return rule.id}).indexOf(+ruleIds);
         return ruleNames += this.rules[index]?.translatedText;
      }
    }
  }

  getServiceNames(serviceIds: Array<string> | string) {
    if(this.services) {
      let ruleNames = "";
      if(serviceIds instanceof Array) {
        serviceIds.forEach((serviceId) => {
          let index = this.services?.map((rule) => {return rule.id}).indexOf(+serviceId);
          ruleNames += this.services[index]?.translatedText + ", ";
        })
        return ruleNames.slice(0, -2);
      }
      else {
        let index = this.services?.map((rule) => {return rule.id}).indexOf(+serviceIds);
        return  ruleNames += this.services[index]?.translatedText;
      }
    }
  }

}
