import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Rule } from './models/rule';
import { Service } from './models/service';
import { Neighborhood } from './models/neighborhood';
import { TranslateService } from '@ngx-translate/core';
import { take } from 'rxjs/operators';

const BASE_API_URL = 'http://localhost:8080/api/';

@Injectable({
  providedIn: 'root'
})
export class MetadataService {

  // allRules: BehaviourSubject<Rule[]>;
  // allServices: Service[];


  constructor(private http: HttpClient, private translateService: TranslateService) { }

  getAllRules(): Observable<Rule[]> {
    return this.http.get<Rule[]>(BASE_API_URL + 'property/rule/');
  }


  getAllServices(): Observable<Service[]> {
    return this.http.get<Service[]>(BASE_API_URL + 'property/service/');
  }

  getAllNeighborhoods(): Observable<Neighborhood[]> {
    return this.http.get<Neighborhood[]>(BASE_API_URL + 'property/neighbourhood/');
  }

  translateMetadataArray(input: Rule[] | Service[]){
    if (!input){
      return;
    }
    input.forEach((elem) => {
      this.translateService.get(elem.name).pipe(take(1)).subscribe((value) => elem.translatedText = value);
    })
  }

}
