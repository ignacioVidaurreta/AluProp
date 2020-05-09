import { Component, OnInit, Input } from '@angular/core';
import { PropertyService } from 'src/app/services/property.service';
import { Property } from 'src/app/models/property';
import { PageRequest } from 'src/app/interfaces/page-request';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-property-grid',
  templateUrl: './property-grid.component.html',
  styleUrls: ['./property-grid.component.scss']
})
export class PropertyGridComponent implements OnInit {

  @Input()
  pageRequest: PageRequest;

  properties: Property[];
  propertiesSub: Subscription;

  constructor(private propertyService: PropertyService) { }

  ngOnInit(): void {
    this.propertiesSub = this.propertyService.getAll().subscribe((properties) => this.properties = properties);
  }

}
