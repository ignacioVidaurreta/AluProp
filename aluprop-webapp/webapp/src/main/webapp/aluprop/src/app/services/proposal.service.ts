import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../interfaces/page-request";
import {Observable} from "rxjs";
import {Proposal} from "../models/proposal";
import {UserProposal} from "../models/userProposal";
import {User} from "../models/user";

const BASE_API_URL = '../assets/json/dummyProposal.json';
const BASE_API_URL_PROPOSAL = 'http://localhost:8080/api/proposal/';
const BASE_API_URL_PROPOSAL_USER_INFO = 'http://localhost:8080/api/guest/';

@Injectable({
  providedIn: 'root'
})
export class ProposalService {

  users: UserProposal[];
  proposal: Proposal;
  creatorUserProposal: UserProposal;

  constructor(private http: HttpClient) { }

  getById(id: number): Observable<Proposal>{
    return this.http.get<Proposal>(BASE_API_URL_PROPOSAL+id);
  }

  getAllUserProposals(id: number): Observable<UserProposal[]>{
    return this.http.get<UserProposal[]>(BASE_API_URL_PROPOSAL+id+'/userProposals');
  }

  getCreatorUserProposal(id: number): Observable<UserProposal>{
    return this.http.get<UserProposal>(BASE_API_URL_PROPOSAL+id+'/creatorUserProposal');
  }

  getGuestUserInfoByProposalId(id: Number): Observable<any>{
    return this.http.get<any>(BASE_API_URL_PROPOSAL_USER_INFO+id+'/userInfo');
  }
}
