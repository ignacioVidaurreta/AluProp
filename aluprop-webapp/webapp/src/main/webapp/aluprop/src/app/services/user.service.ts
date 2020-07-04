import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../interfaces/page-request";
import {Observable} from "rxjs";
import {PageResponse} from "../interfaces/page-response";
import {User} from "../models/user";
import {UserProposal} from "../models/userProposal";
import {Proposal} from "../models/proposal";
import {Property} from "../models/property";

const BASE_API_URL = 'http://localhost:8080/api/user';
const BASE_API_URL_USER_PROPOSALS = 'http://localhost:8080/api/guest/proposals';
const BASE_API_URL_PROPOSALS = 'http://localhost:8080/api/host/proposals';
const BASE_API_URL_OWNED_PROPERTIES = 'http://localhost:8080/api/host/properties';
const BASE_API_URL_INTERESTED_PROPERTIES = 'http://localhost:8080/api/guest/interests';
const BASE_API_URL_IS_USER_LOGGED_IN = 'http://localhost:8080/api/user/loginStatus';


@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getUserById(id: number): Observable<User>{
    console.log(BASE_API_URL+id);
    return this.http.get<User>(BASE_API_URL+id);
  }

  getAllProposalsFromUserProposals(): Observable<Proposal[]>{
    console.log(BASE_API_URL_USER_PROPOSALS);
    return this.http.get<Proposal[]>(BASE_API_URL_USER_PROPOSALS);
  }

  getAllProposals(): Observable<Proposal[]>{
    return this.http.get<Proposal[]>(BASE_API_URL_PROPOSALS);
  }

  getAllOwnedProperties(): Observable<Property[]>{
    return this.http.get<Property[]>(BASE_API_URL_OWNED_PROPERTIES);
  }

  getAllInterestedProperties(): Observable<Property[]>{
    return this.http.get<Property[]>(BASE_API_URL_INTERESTED_PROPERTIES);
  }

  isUserLoggedIn(): Observable<boolean>{
    return this.http.get<boolean>(BASE_API_URL_IS_USER_LOGGED_IN);
  }
}
