import { Component, OnInit } from '@angular/core';
import {ProposalService} from "../../services/proposal.service";
import {PageEvent} from "@angular/material/paginator";
import {Subscription} from "rxjs";
import {Proposal} from "../../models/proposal";
import { ActivatedRoute } from "@angular/router";
import {UserProposal} from "../../models/userProposal";
import {User} from "../../models/user";

@Component({
  selector: 'app-proposal',
  templateUrl: './proposal.component.html',
  styleUrls: ['./proposal.component.scss']
})
export class ProposalComponent implements OnInit {
  proposalId: number;
  proposal: Proposal;
  proposalSub: Subscription;

  constructor(private proposalService: ProposalService, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.createPageSubscription();
    this.proposalId = +this.route.snapshot.paramMap.get("id");
  }

  ngOnDestroy(): void {
    this.proposalSub.unsubscribe();
  }

  onPageChange(pageEvent: PageEvent){
    this.proposalSub.unsubscribe();
    this.createPageSubscription();
  }

  createPageSubscription(){
    this.proposalSub = this.proposalService.getById(this.proposalId).subscribe((proposal) => {
      this.proposal = proposal;
    });
  }

}
