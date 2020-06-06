import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../interfaces/page-request";
import {Observable} from "rxjs";
import {Proposal} from "../models/proposal";
import {UserProposal} from "../models/userProposal";

const BASE_API_URL = '../assets/json/dummyProposal.json';
const BASE_API_URL_USER_PROPOSALS = '../assets/json/dummyUserProposals.json';


@Injectable({
  providedIn: 'root'
})
export class ProposalService {

  users: UserProposal[];
  proposal: Proposal;
  creatorUserProposal: UserProposal;

  constructor(private http: HttpClient) { }

  getById(id: number): Observable<Proposal>{
    return this.http.get<Proposal>(BASE_API_URL);
  }

  getAllUserProposals(id: number): Observable<UserProposal[]>{
    return this.http.get<UserProposal[]>(BASE_API_URL_USER_PROPOSALS);
  }
  // TODO: the creator's userProposal must be appended in front of the proposal's userProposals[]
  // this.proposal.creator.userProposals?.forEach(function(value){
  //   if(value.proposal.id == this.proposal.id)
  //     this.creatorUserProposal = value;
  // })
  // this.users = [this.creatorUserProposal].concat(this.proposal.userProposals);
}
