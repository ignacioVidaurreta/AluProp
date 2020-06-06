import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../interfaces/page-request";
import {Observable} from "rxjs";
import {PageResponse} from "../interfaces/page-response";
import {User} from "../models/user";
import {UserProposal} from "../models/userProposal";
import {Proposal} from "../models/proposal";
import {Property} from "../models/property";

const BASE_API_URL = '../assets/json/dummyUser.json';
const BASE_API_URL_USER_PROPOSALS = '../assets/json/dummyUserProposals.json';
const BASE_API_URL_PROPOSALS = '../assets/json/dummyProposalsForUserService.json';
const BASE_API_URL_OWNED_PROPERTIES = '../assets/json/dummyOwnedProperties.json';
const BASE_API_URL_INTERESTED_PROPERTIES = '../assets/json/dummyInterestedProperties.json';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getUserById(id: number): Observable<User>{
    return this.http.get<User>(BASE_API_URL);
  }

  getAllUserProposalsByUserId(id: number): Observable<UserProposal[]>{
    return this.http.get<UserProposal[]>(BASE_API_URL_USER_PROPOSALS);
  }

  getAllProposalsByUserId(id: number): Observable<Proposal[]>{
    return this.http.get<Proposal[]>(BASE_API_URL_PROPOSALS);
  }

  getAllOwnedPropertiesByUserId(id: number): Observable<Property[]>{
    return this.http.get<Property[]>(BASE_API_URL_OWNED_PROPERTIES);
  }

  getAllInterestedPropertiesByUserId(id: number): Observable<Property[]>{
    return this.http.get<Property[]>(BASE_API_URL_INTERESTED_PROPERTIES);
  }
}
