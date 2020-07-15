import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {Proposal, ProposalState} from "../../../models/proposal";
import {UserProposal} from "../../../models/userProposal";
import {ProposalService} from "../../../services/proposal.service";
import {ActivatedRoute} from "@angular/router";
import {Observable, Subscription} from "rxjs";
import {User} from "../../../models/user";
import * as moment from 'moment';
import {take} from "rxjs/operators";

@Component({
  selector: 'app-proposal-users-table',
  templateUrl: './proposal-users-table.component.html',
  styleUrls: ['./proposal-users-table.component.scss']
})
export class ProposalUsersTableComponent implements OnInit {

  displayedColumns: string[] = ['user','information', 'response'];

  @Input() proposalState: ProposalState;
  @Input() proposalCreator: User;
  @Input() reload: Observable<void>;

  dataSource;
  userProposals: UserProposal[];
  userProposalsSub: Subscription;
  creator: User;
  creatorSub: Subscription;
  creatorUserProposal: UserProposal;
  proposalId: number;
  private reloadSubscription: Subscription;


  constructor(private proposalService: ProposalService, private route: ActivatedRoute) {
    this.userProposals = [];
    this.creatorUserProposal = new UserProposal();
    this.dataSource = [];
  }

  ngOnInit(): void {
    this.proposalId = +this.route.snapshot.paramMap.get("id");
    this.reloadSubscription = this.reload.subscribe(() => this.onPageChange());
    this.createPageSubscription();
  }

  ngOnDestroy(): void {
    this.userProposalsSub?.unsubscribe();
    this.creatorSub?.unsubscribe();
    this.reloadSubscription?.unsubscribe();
  }

  onPageChange(){
    this.userProposalsSub?.unsubscribe();
    this.creatorSub?.unsubscribe();
    this.createPageSubscription();
  }

  createPageSubscription(){
    this.userProposalsSub = this.proposalService.getAllUserProposals(this.proposalId).subscribe((userProposals) => {
      this.userProposals = userProposals;
      this.creatorSub = this.proposalService.getCreatorUserProposal(this.proposalId).subscribe((creatorUserProposal) => {
        this.creatorUserProposal= creatorUserProposal;
        this.dataSource = new MatTableDataSource<UserProposal>([this.creatorUserProposal].concat(this.userProposals));
      });
    });
  }

  ageFromDateOfBirthday(dateOfBirth: any): number {
    return moment().diff(dateOfBirth, 'years');
  }

}
