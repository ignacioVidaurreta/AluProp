import { Component, OnInit } from '@angular/core';
import { PropertyType, PrivacyLevel, Property } from '../../models/property';
import { Validators, FormControl, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { Rule } from 'src/app/models/rule';
import { Service } from 'src/app/models/service';
import { Neighborhood } from 'src/app/models/neighborhood';
import { Image } from 'src/app/models/image';
import { PropertyService } from 'src/app/services/property.service';
import { MetadataService } from 'src/app/metadata.service';
import { TranslateService } from '@ngx-translate/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-create-property',
  templateUrl: './create-property.component.html',
  styleUrls: ['./create-property.component.scss']
})
export class CreatePropertyComponent implements OnInit {

  propertyTypeOptions = [
    PropertyType.House,
    PropertyType.Apartment,
    PropertyType.Loft
  ];
  privacyLevelOption = PrivacyLevel;

  neighborhoods: Neighborhood[];
  neighborhoodsSub: Subscription;
  rules: Rule[] = [];
  rulesSub: Subscription;
  services: Service[] = [];
  servicesSub: Subscription;

  languageChangedSub: Subscription;

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

  tooManyFiles =  false;
  notEnoughFiles =  false;
  currentlyUploadedImages: string[] = [];
  
  constructor(private propertyService: PropertyService, 
              private metadataService: MetadataService, 
              private translateService: TranslateService,
              public domSanitizer: DomSanitizer) {
    this.createdProperty = new Property();
    this.currentlyUploadedImages = [];
    this.languageChangedSub = translateService.onLangChange.subscribe((newLang) => this.translateRulesAndServices());
  }

  translateRulesAndServices(){
    this.metadataService.translateMetadataArray(this.rules);
    this.metadataService.translateMetadataArray(this.services);
  }
  
  ngOnInit(): void {
    // this.formChangesSub = this.createPropertyForm.valueChanges.subscribe((filters) => {
    //   // this.filters.emit(filters)
    // });
    this.rulesSub = this.metadataService.getAllRules().subscribe((rules) => {
      this.rules = rules;
      this.translateRulesAndServices();
    });
    this.servicesSub = this.metadataService.getAllServices().subscribe((services) => {
      this.services = services;
      this.translateRulesAndServices();
    });
    this.neighborhoodsSub = this.metadataService.getAllNeighborhoods().subscribe((neighborhoods) => this.neighborhoods = neighborhoods);

  }

  ngOnDestroy(){
    if (this.publishPropertySub) { this.publishPropertySub.unsubscribe();}
    if (this.rulesSub) { this.rulesSub.unsubscribe();}
    if (this.servicesSub) { this.servicesSub.unsubscribe();}
    if (this.neighborhoodsSub) { this.neighborhoodsSub.unsubscribe();}
    // if (this.formChangesSub) { this.formChangesSub.unsubscribe();}
  }

  removeSelectedImage(i) {
    this.currentlyUploadedImages.splice(i, 1);
  }

  detectFiles(event) {
    let files = event.target.files;
    if (files) {
      if (this.currentlyUploadedImages.length + files.length > 4){
        this.tooManyFiles = true;
        this.notEnoughFiles = false;
        return;
      }
      this.tooManyFiles = false;
      this.notEnoughFiles = false;
      for (let file of files) {
          let reader = new FileReader();
          reader.onload = (e: any) => {
            this.currentlyUploadedImages.push(e.target.result);
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

  removeTranslatedTextAttribute(input: Rule | Service){
    let result = {... input};
    delete result.translatedText;
    return result;
  }

  generatePropertyFromForm() {
    console.log(this.createPropertyForm.get('name'));
    if (this.currentlyUploadedImages.length === 0){
      this.tooManyFiles = false;
      this.notEnoughFiles = true;
      return;
    }
    this.createdProperty.images = [];
    this.currentlyUploadedImages.forEach(
      (image, index) => {
        if (index === 0){
          this.createdProperty.mainImage = <Image>{id: index, image: image.split(',')[1]};
        }
        this.createdProperty.images.push(<Image>{id: index, image: image.split(',')[1]});
      }
    );

    console.log(this.createPropertyForm.controls['rules'].value);
    console.log(this.createPropertyForm.controls['propertyType'].value);

    this.createdProperty.description = this.createPropertyForm.controls['name'].value;
    this.createdProperty.caption = this.createPropertyForm.controls['description'].value;
    this.createdProperty.propertyType = this.createPropertyForm.controls['propertyType'].value;
    this.createdProperty.neighbourhood = this.createPropertyForm.controls['neighborhood'].value;
    this.createdProperty.privacyLevel = this.createPropertyForm.controls['privacy'].value;
    this.createdProperty.capacity = this.createPropertyForm.controls['capacity'].value;
    this.createdProperty.price = this.createPropertyForm.controls['rent'].value;
    this.createdProperty.rules = this.createPropertyForm.controls['rules'].value;
    this.createdProperty.services = this.createPropertyForm.controls['services'].value;
  }

}
