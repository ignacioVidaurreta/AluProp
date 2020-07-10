import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../interfaces/page-request";
import {Observable} from "rxjs";
import {PageResponse} from "../interfaces/page-response";
import {User} from "../models/user";
import {UserProposal} from "../models/userProposal";
import {Proposal} from "../models/proposal";
import {Property} from "../models/property";
import { environment } from "../../environments/environment"

const BASE_API_URL = environment.apiUrl;
const BASE_API_URL_USER = `${BASE_API_URL}user/`;
const BASE_API_URL_USER_PROPOSALS = `${BASE_API_URL}guest/proposals`;
const BASE_API_URL_PROPOSALS = `${BASE_API_URL}host/proposals`;
const BASE_API_URL_OWNED_PROPERTIES = `${BASE_API_URL}host/properties`;
const BASE_API_URL_INTERESTED_PROPERTIES = `${BASE_API_URL}guest/interests`;
const BASE_API_URL_IS_USER_LOGGED_IN = `${BASE_API_URL}user/loginStatus`;


@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getUserById(id: number): Observable<User>{
    console.log(BASE_API_URL_USER + id);
    return this.http.get<User>(BASE_API_URL_USER + id);
  }

  getAllProposalsFromUserProposals(pageRequest: PageRequest): Observable<PageResponse<Proposal>>{
    let params = {pageSize: String(pageRequest.pageSize), pageNumber: String(pageRequest.pageNumber) };
    return this.http.get<PageResponse<Proposal>>(BASE_API_URL_USER_PROPOSALS, {params: params});
  }

  getAllProposals(pageRequest: PageRequest): Observable<PageResponse<Proposal>>{
    let params = {pageSize: String(pageRequest.pageSize), pageNumber: String(pageRequest.pageNumber) };
    return this.http.get<PageResponse<Proposal>>(BASE_API_URL_PROPOSALS, {params: params});
  }

  getAllOwnedProperties(pageRequest: PageRequest): Observable<PageResponse<Property>>{
    let params = {pageSize: String(pageRequest.pageSize), pageNumber: String(pageRequest.pageNumber) };
    return this.http.get<PageResponse<Property>>(BASE_API_URL_OWNED_PROPERTIES, {params: params});
  }

  getAllInterestedProperties(pageRequest: PageRequest): Observable<PageResponse<Property>>{
    let params = {pageSize: String(pageRequest.pageSize), pageNumber: String(pageRequest.pageNumber) };
    return this.http.get<PageResponse<Property>>(BASE_API_URL_INTERESTED_PROPERTIES, {params: params});
  }

  isUserLoggedIn(): Observable<boolean>{
    return this.http.get<boolean>(BASE_API_URL_IS_USER_LOGGED_IN);
  }
}
