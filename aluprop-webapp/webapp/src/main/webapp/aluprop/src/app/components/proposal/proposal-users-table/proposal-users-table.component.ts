import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {Proposal, ProposalState} from "../../../models/proposal";
import {UserProposal} from "../../../models/userProposal";
import {ProposalService} from "../../../services/proposal.service";
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-proposal-users-table',
  templateUrl: './proposal-users-table.component.html',
  styleUrls: ['./proposal-users-table.component.scss']
})
export class ProposalUsersTableComponent implements OnInit {

  displayedColumns: string[] = ['user','information', 'contactInfo', 'response'];

  @Input() proposalState: ProposalState;

  dataSource;
  userProposals: UserProposal[];
  userProposalsSub: Subscription;
  proposalId: number;


  constructor(private proposalService: ProposalService, private route: ActivatedRoute) {
    this.userProposals = [];
    this.dataSource = [];
  }

  ngOnInit(): void {
    this.createPageSubscription();
    this.proposalId = +this.route.snapshot.paramMap.get("id");
  }

  ngOnDestroy(): void {
    this.userProposalsSub.unsubscribe();
  }

  onPageChange(pageEvent: PageEvent){
    this.userProposalsSub.unsubscribe();
    this.createPageSubscription();
  }

  createPageSubscription(){
    this.userProposalsSub = this.proposalService.getAllUserProposals(this.proposalId).subscribe((userProposals) => {
      this.dataSource = new MatTableDataSource<UserProposal>(userProposals);
    });
  }

}
