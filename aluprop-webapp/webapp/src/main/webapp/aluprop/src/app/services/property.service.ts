import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PageRequest } from '../interfaces/page-request';
import { Observable } from 'rxjs';
import { Property } from '../models/property';
import { PageResponse } from '../interfaces/page-response';
import {Proposal} from "../models/proposal";
import {User} from "../models/user";

const BASE_API_URL = 'http://localhost:8080/api';
const BASE_API_URL_PROPERTY = 'http://localhost:8080/api/property/';

const JSON_ALL_PROPERTIES = '../assets/json/dummyProperties.json';

const httpOptions = {
  headers: new HttpHeaders({
    'Access-Control-Allow-Origin':'*',
    'Authorization':'authkey',
    'userid':'1'
  })
};

@Injectable({
  providedIn: 'root'
})
export class PropertyService {

  constructor(private http: HttpClient) { }

  getAll(pageRequest: PageRequest): Observable<PageResponse<Property>>{
    let params = {pageSize: String(pageRequest.pageSize), pageNumber: String(pageRequest.pageNumber) };
    return this.http.get<PageResponse<Property>>(BASE_API_URL + '/property/', {params: params});
  }

  search(pageRequest: PageRequest, searchParams: any){
    let params = {pageSize: String(pageRequest.pageSize), pageNumber: String(pageRequest.pageNumber), ...searchParams};
    console.log(params);
    return this.http.get<PageResponse<Property>>(BASE_API_URL + '/property/search/', {params: params});
  }

  getById(id: number): Observable<Property>{
    console.log(BASE_API_URL_PROPERTY+id);
    return this.http.get<Property>(BASE_API_URL_PROPERTY+id);
  }

  publishProperty(property: Property): Observable<Property> {
    console.log('Posting property:');
    console.log(property);
    return this.http.post<Property>(BASE_API_URL + '/property/', property);
  }

  getInterestedUsersByPropertyId(id: number): Observable<User[]>{
    console.log(BASE_API_URL_PROPERTY+id);
    return this.http.get<User[]>(BASE_API_URL_PROPERTY+id+'/interestedUsers');
  }
}
