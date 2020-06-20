import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {PropertyService} from "../../services/property.service";
import {PageEvent} from "@angular/material/paginator";
import {Subscription} from "rxjs";
import {Property} from "../../models/property";

@Component({
  selector: 'app-detailed-property',
  templateUrl: './detailed-property.component.html',
  styleUrls: ['./detailed-property.component.scss']
})
export class DetailedPropertyComponent implements OnInit {

  propertyId: number;
  property: Property;
  propertySub: Subscription;

  constructor(private propertyService: PropertyService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.propertyId = +this.route.snapshot.paramMap.get("id");
    console.log(this.propertyId);
    this.createPageSubscription();
  }

  ngOnDestroy(): void {
    this.propertySub.unsubscribe();
  }

  onPageChange(pageEvent: PageEvent){
    this.propertySub.unsubscribe();
    this.createPageSubscription();
  }

  createPageSubscription(){
    this.propertySub = this.propertyService.getById(this.propertyId).subscribe((property) => {
      console.log('holaaa');
      this.property = property;
      console.log(property);
    });
  }

}
