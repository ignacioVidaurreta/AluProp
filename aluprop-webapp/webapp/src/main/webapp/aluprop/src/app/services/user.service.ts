import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../interfaces/page-request";
import {Observable} from "rxjs";
import {PageResponse} from "../interfaces/page-response";
import {User} from "../models/user";

const BASE_API_URL = '../assets/json/dummyUser.json';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getAll(pageRequest?: PageRequest): Observable<PageResponse<User>>{
    return this.http.get<PageResponse<User>>(BASE_API_URL);
  }

}
