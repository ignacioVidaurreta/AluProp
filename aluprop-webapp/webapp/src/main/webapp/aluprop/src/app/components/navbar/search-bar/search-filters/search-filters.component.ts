import { Component, OnInit } from '@angular/core';
import { PropertyType, PrivacyLevel } from '../../../../models/property';
import { Neighborhood } from 'src/app/models/neighborhood';
import { Rule } from 'src/app/models/rule';
import { Service } from 'src/app/models/service';

@Component({
  selector: 'app-search-filters',
  templateUrl: './search-filters.component.html',
  styleUrls: ['./search-filters.component.scss']
})
export class SearchFiltersComponent implements OnInit {

  propertyTypeOptions = PropertyType;
  privacyLevelOption = PrivacyLevel;

  neighborhoods: Neighborhood[];
  rules: Rule[];
  services: Service[];

  constructor() { }

  ngOnInit(): void {
  }

}
