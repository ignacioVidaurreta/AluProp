import { Component, OnInit, Output, OnDestroy } from '@angular/core';
import { PropertyType, PrivacyLevel } from '../../../../models/property';
import { Neighborhood } from 'src/app/models/neighborhood';
import { Rule } from 'src/app/models/rule';
import { Service } from 'src/app/models/service';
import { FormControl, Validators, FormGroup } from '@angular/forms';
import { EventEmitter } from '@angular/core';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-search-filters',
  templateUrl: './search-filters.component.html',
  styleUrls: ['./search-filters.component.scss']
})
export class SearchFiltersComponent implements OnInit, OnDestroy {

  propertyTypeOptions = PropertyType;
  privacyLevelOption = PrivacyLevel;

  neighborhoods: Neighborhood[];
  rules: Rule[] = [{id: 0, name: 'Mascotas Prohibidas', properties: []}, {id: 1, name: 'Fiestas Prohibidas', properties: []}];
  services: Service[];

  filterForm = new FormGroup({
    propertyType: new FormControl(''),
    neighborhood: new FormControl(''),
    privacy: new FormControl(''),
    maxCapacity: new FormControl('', [Validators.pattern('[0-9]*')]),
    minRent: new FormControl('', [Validators.pattern('[0-9]*')]),
    maxPrice: new FormControl('', [Validators.pattern('[0-9]*')]),
    rules: new FormControl(this.rules),
    services: new FormControl(this.services)
  });
  formChangesSub: Subscription;
  
  @Output()
  filters = new EventEmitter();
  
  constructor() { }
  
  ngOnInit(): void {
    this.formChangesSub = this.filterForm.valueChanges.subscribe((filters) => this.filters.emit(filters));
  }

  ngOnDestroy(){
    this.formChangesSub.unsubscribe();
  }
  
}
