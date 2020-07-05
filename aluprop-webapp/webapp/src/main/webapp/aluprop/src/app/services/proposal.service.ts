import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../interfaces/page-request";
import {Observable} from "rxjs";
import {Proposal} from "../models/proposal";
import {UserProposal} from "../models/userProposal";
import {User} from "../models/user";
import {Property} from "../models/property";

const BASE_API_URL = 'http://localhost:8080/api/';
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

  createProposal(invitedUserIds: number[], propertyId: number): Observable<Proposal> {
    console.log('Creating proposal:');
    console.log(invitedUserIds);
    const payload = {inviteeIds: invitedUserIds};
    return this.http.post<Proposal>(BASE_API_URL + 'guest/proposal/' + propertyId, payload);
  }
}
