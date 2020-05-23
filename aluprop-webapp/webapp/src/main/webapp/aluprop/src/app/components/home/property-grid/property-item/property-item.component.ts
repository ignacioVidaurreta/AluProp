import { Component, OnInit, Input, } from '@angular/core';
import { Property } from 'src/app/models/property';

@Component({
  selector: 'app-property-item',
  templateUrl: './property-item.component.html',
  styleUrls: ['./property-item.component.scss']
})
export class PropertyItemComponent implements OnInit {

  @Input()
  property: Property;

  constructor() { }

  ngOnInit(): void { }
}