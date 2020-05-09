import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PageRequest } from '../interfaces/page-request';
import { Observable } from 'rxjs';
import { Property } from '../models/property';

const BASE_API_URL = 'http://localhost:8080/api/'

@Injectable({
  providedIn: 'root'
})
export class PropertyService {

  constructor(private http: HttpClient) { }

  getAll(pageRequest?: PageRequest): Observable<Property[]>{
    return this.http.get<Property[]>(BASE_API_URL + '/properties');
  }
}
