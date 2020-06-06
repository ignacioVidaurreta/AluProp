import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PageRequest} from "../interfaces/page-request";
import {Observable} from "rxjs";
import {Proposal} from "../models/proposal";
import {UserProposal} from "../models/userProposal";
import {User} from "../models/user";

const BASE_API_URL = '../assets/json/dummyProposal.json';
const BASE_API_URL_USER_PROPOSALS = '../assets/json/dummyUserProposals.json';
const BASE_API_URL_PROPOSAL_CREATOR = '../assets/json/dummyProposalCreator.json';
const BASE_API_URL_PROPOSAL_CREATOR_USER_PROPOSAL = '../assets/json/dummyProposalCreatorUserProposal.json';

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

  getCreator(id: number): Observable<User>{
    return this.http.get<User>(BASE_API_URL_PROPOSAL_CREATOR);
  }

  getCreatorUserProposal(id: number): Observable<UserProposal>{
    return this.http.get<UserProposal>(BASE_API_URL_PROPOSAL_CREATOR_USER_PROPOSAL);
  }
}
