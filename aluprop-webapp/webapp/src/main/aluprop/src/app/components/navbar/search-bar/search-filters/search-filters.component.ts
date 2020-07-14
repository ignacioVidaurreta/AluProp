import {Component, OnInit, Output, OnDestroy, Input} from '@angular/core';
import { PropertyType, PrivacyLevel } from '../../../../models/property';
import { Neighborhood } from 'src/app/models/neighborhood';
import { Rule } from 'src/app/models/rule';
import { Service } from 'src/app/models/service';
import { FormControl, Validators, FormGroup } from '@angular/forms';
import { EventEmitter } from '@angular/core';
import {Observable, Subscription} from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { MetadataService } from 'src/app/metadata.service';

@Component({
  selector: 'app-search-filters',
  templateUrl: './search-filters.component.html',
  styleUrls: ['./search-filters.component.scss']
})
export class SearchFiltersComponent implements OnInit, OnDestroy {

  propertyTypeOptions = PropertyType;
  privacyLevelOption = PrivacyLevel;

  @Input() resetFilters: Observable<void>;
  resetFiltersSub: Subscription;

  neighborhoods: Neighborhood[];
  neighborhoodsSub: Subscription;
  rules: Rule[] = [];
  rulesSub: Subscription;
  services: Service[] = [];
  servicesSub: Subscription;

  languageChangedSub: Subscription;

  filterForm = new FormGroup({
    propertyType: new FormControl(''),
    neighbourhoodId: new FormControl(''),
    privacy: new FormControl(''),
    maxCapacity: new FormControl('', [Validators.pattern('[0-9]*')]),
    minPrice: new FormControl('', [Validators.pattern('[0-9]*')]),
    maxPrice: new FormControl('', [Validators.pattern('[0-9]*')]),
    rules: new FormControl(this.rules),
    services: new FormControl(this.services)
  });
  formChangesSub: Subscription;

  @Output()
  filters = new EventEmitter();

  constructor(translateService: TranslateService, private metadataService: MetadataService) {
    this.languageChangedSub = translateService.onLangChange.subscribe((newLang) => this.translateRulesAndServices());
  }

  translateRulesAndServices(){
    this.metadataService.translateMetadataArray(this.rules);
    this.metadataService.translateMetadataArray(this.services);
  }

  ngOnInit(): void {
    this.formChangesSub = this.filterForm.valueChanges.subscribe((filters) => this.filters.emit(filters));
    this.rulesSub = this.metadataService.getAllRules().subscribe((rules) => {
      this.rules = rules;
      this.translateRulesAndServices();
    });
    this.servicesSub = this.metadataService.getAllServices().subscribe((services) => {
      this.services = services;
      this.translateRulesAndServices();
    });
    this.neighborhoodsSub = this.metadataService.getAllNeighborhoods().subscribe((neighborhoods) => this.neighborhoods = neighborhoods);
    this.resetFiltersSub = this.resetFilters.subscribe(() => {this.filterForm.reset()});
  }

  ngOnDestroy(){
    if (this.formChangesSub) { this.formChangesSub.unsubscribe();}
    if (this.rulesSub) { this.rulesSub.unsubscribe();}
    if (this.servicesSub) { this.servicesSub.unsubscribe();}
    if (this.neighborhoodsSub) { this.neighborhoodsSub.unsubscribe();}
  }

}
