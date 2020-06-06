import { Component, OnInit, Output, OnDestroy } from '@angular/core';
import { PropertyType, PrivacyLevel } from '../../../../models/property';
import { Neighborhood } from 'src/app/models/neighborhood';
import { Rule } from 'src/app/models/rule';
import { Service } from 'src/app/models/service';
import { FormControl, Validators, FormGroup } from '@angular/forms';
import { EventEmitter } from '@angular/core';
import { Subscription } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';

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
    property_type: new FormControl(''),
    neighborhood: new FormControl(''),
    privacy: new FormControl(''),
    max_capacity: new FormControl('', [Validators.pattern('[0-9]*')]),
    min_rent: new FormControl('', [Validators.pattern('[0-9]*')]),
    max_rent: new FormControl('', [Validators.pattern('[0-9]*')]),
    rules: new FormControl(this.rules),
    services: new FormControl(this.services)
  });
  formChangesSub: Subscription;
  
  @Output()
  filters = new EventEmitter();
  
  constructor(private translateService: TranslateService) { }
  
  ngOnInit(): void {
    this.formChangesSub = this.filterForm.valueChanges.subscribe((filters) => this.filters.emit(filters));
  }

  ngOnDestroy(){
    this.formChangesSub.unsubscribe();
  }
  
}
