import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../interfaces/page-request";
import {Observable} from "rxjs";
import {Proposal} from "../models/proposal";
import {UserProposal} from "../models/userProposal";
import { environment } from "../../environments/environment"


const BASE_API_URL = environment.apiUrl;
const BASE_API_URL_PROPOSAL = `${BASE_API_URL}proposal/`;
const BASE_API_URL_PROPOSAL_USER_INFO = `${BASE_API_URL}guest/`;

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
    const payload = {inviteeIds: invitedUserIds};
    return this.http.post<Proposal>(BASE_API_URL + 'guest/proposal/' + propertyId, payload);
  }

  acceptProposalGuest(proposalId: number) {
    return this.http.post(BASE_API_URL + 'guest/' + proposalId + '/accept', {});
  }

  declineProposalGuest(proposalId: number) {
    return this.http.post(BASE_API_URL + 'guest/' + proposalId + '/decline', {});
  }

  dropProposal(proposalId: number) {
    return this.http.post(BASE_API_URL + 'guest/' + proposalId + '/cancel', {});
  }

  acceptProposalHost(proposalId: number) {
    return this.http.post(BASE_API_URL + 'host/' + proposalId + '/accept', {});
  }

  declineProposalHost(proposalId: number) {
    return this.http.post(BASE_API_URL + 'host/' + proposalId + '/decline', {});
  }

}
