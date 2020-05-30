import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PageRequest } from '../interfaces/page-request';
import { Observable } from 'rxjs';
import { Property } from '../models/property';
import { PageResponse } from '../interfaces/page-response';

const BASE_API_URL = '../assets/json/dummyProperties.json';

@Injectable({
  providedIn: 'root'
})
export class PropertyService {

  constructor(private http: HttpClient) { }

  getAll(pageRequest?: PageRequest): Observable<PageResponse<Property>>{
    return this.http.get<PageResponse<Property>>(BASE_API_URL);
  }
}
