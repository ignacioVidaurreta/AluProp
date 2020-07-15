import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PageRequest } from '../interfaces/page-request';
import { Observable } from 'rxjs';
import { Property } from '../models/property';
import { PageResponse } from '../interfaces/page-response';
import {Proposal} from "../models/proposal";
import {User} from "../models/user";
import { environment } from './../../environments/environment'

const BASE_API_URL = environment.apiUrl;
const BASE_API_URL_PROPERTY = `${BASE_API_URL}property/`;
const BASE_API_URL_PROPERTY_IS_CURRENT_USER_INTERESTED = `${BASE_API_URL}guest/interested/`;



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
    return this.http.get<PageResponse<Property>>(BASE_API_URL + 'property/', {params: params});
  }

  search(pageRequest: PageRequest, searchParams: any){
    let params = {pageSize: String(pageRequest.pageSize), pageNumber: String(pageRequest.pageNumber), ...searchParams};
    return this.http.get<PageResponse<Property>>(BASE_API_URL + 'property/search/', {params: params});
  }

  getById(id: number): Observable<Property>{
    return this.http.get<Property>(BASE_API_URL_PROPERTY+id);
  }

  publishProperty(property: Property): Observable<Property> {
    return this.http.post<Property>(BASE_API_URL + 'host/createProperty/', property);
  }

  getInterestedUsersByPropertyId(id: number): Observable<User[]> {
    return this.http.get<User[]>(BASE_API_URL_PROPERTY + id + '/interestedUsers');
  }

  isCurrentUserInterested(id: number): Observable<boolean> {
    return this.http.get<boolean>(BASE_API_URL_PROPERTY_IS_CURRENT_USER_INTERESTED+id);
  }

  changePropertyAvailability(propertyId: number) {
    return this.http.post(BASE_API_URL + 'host/changeStatus/' + propertyId, {});
  }

  markInterest(propertyId: number) {
    return this.http.post(BASE_API_URL + 'guest/' + propertyId + '/interested', {});
  }

  markUninterest(propertyId: number) {
    return this.http.post(BASE_API_URL + 'guest/' + propertyId + '/uninterested', {});
  }

  deleteProperty(propertyId: number) {
    return this.http.post(BASE_API_URL + 'host/delete/' + propertyId, {});
  }

}
