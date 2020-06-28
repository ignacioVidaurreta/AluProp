import { Component, OnInit } from '@angular/core';
import { PropertyType, PrivacyLevel, Property } from '../../models/property';
import { Validators, FormControl, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { Rule } from 'src/app/models/rule';
import { Service } from 'src/app/models/service';
import { Neighborhood } from 'src/app/models/neighborhood';
import { Image } from 'src/app/models/image';
import { PropertyService } from 'src/app/services/property.service';

@Component({
  selector: 'app-create-property',
  templateUrl: './create-property.component.html',
  styleUrls: ['./create-property.component.scss']
})
export class CreatePropertyComponent implements OnInit {

  propertyTypeOptions = PropertyType;
  privacyLevelOption = PrivacyLevel;

  neighborhoods: Neighborhood[];
  rules: Rule[] = [{id: 0, name: 'Mascotas Prohibidas', properties: []}, {id: 1, name: 'Fiestas Prohibidas', properties: []}];
  services: Service[];

  createPropertyForm = new FormGroup({
    pictures: new FormControl(''),
    
    name: new FormControl('', [Validators.required]),
    description: new FormControl('', [Validators.required]),
    propertyType: new FormControl('', [Validators.required]),
    neighborhood: new FormControl('', [Validators.required]),
    privacy: new FormControl('', [Validators.required]),
    capacity: new FormControl('', [Validators.pattern('[0-9]*'), Validators.required]),
    rent: new FormControl('', [Validators.pattern('[0-9]*'), Validators.required]),
    rules: new FormControl(this.rules),
    services: new FormControl(this.services)
  });
  formChangesSub: Subscription;

  createdProperty: Property;
  publishPropertySub: Subscription;
  
  constructor(private propertyService: PropertyService) {
    this.createdProperty = new Property();
  }
  
  ngOnInit(): void {
    this.formChangesSub = this.createPropertyForm.valueChanges.subscribe((filters) => {
      // this.filters.emit(filters)
    });
  }

  ngOnDestroy(){
    if (this.publishPropertySub) { this.publishPropertySub.unsubscribe();}
    if (this.formChangesSub) { this.formChangesSub.unsubscribe();}
  }

  detectFiles(event) {
    this.createdProperty.image = [];
    let files = event.target.files;
    if (files) {
        for (let file of files) {
            let reader = new FileReader();
            reader.onload = (e: any) => {
              this.createdProperty.image.push({id: 0, image: e.target.result});
            }
            reader.readAsDataURL(file);
        }
    }
  }

  publishProperty(){
    this.generatePropertyFromForm();
    this.publishPropertySub = this.propertyService.publishProperty(this.createdProperty).subscribe(
      (property) => {
        console.log(property);
      });
  }

  generatePropertyFromForm() {
    console.log(this.createPropertyForm.get('name'));
    this.createdProperty.description = this.createPropertyForm.controls['name'].value;
    this.createdProperty.caption = this.createPropertyForm.controls['description'].value;
    this.createdProperty.propertyType = this.createPropertyForm.controls['propertyType'].value;
    this.createdProperty.neighbourhood = this.createPropertyForm.controls['neighborhood'].value !== ""? <Neighborhood>{id: this.createPropertyForm.controls['neighborhood'].value} : null;
    this.createdProperty.privacyLevel = this.createPropertyForm.controls['privacy'].value;
    this.createdProperty.capacity = this.createPropertyForm.controls['capacity'].value;
    this.createdProperty.price = this.createPropertyForm.controls['rent'].value;
    this.createdProperty.rules = this.createPropertyForm.controls['rules'].value;
    this.createdProperty.services = this.createPropertyForm.controls['services'].value;
  }

}
