import { Component, OnInit } from '@angular/core';
import {ProposalService} from "../../services/proposal.service";
import {PageEvent} from "@angular/material/paginator";
import {Subscription} from "rxjs";
import {Proposal} from "../../models/proposal";
import { ActivatedRoute } from "@angular/router";
import {UserProposal} from "../../models/userProposal";
import {User} from "../../models/user";
import {UserService} from "../../services/user.service";
import {AuthenticationService} from "../../services/authentication.service";

@Component({
  selector: 'app-proposal',
  templateUrl: './proposal.component.html',
  styleUrls: ['./proposal.component.scss']
})
export class ProposalComponent implements OnInit {
  proposalId: number;
  proposal: Proposal;
  proposalSub: Subscription;
  currentUser: User;
  currentUserSub: Subscription;
  budget: number;
  isInvited: boolean;
  hasReplied: boolean;
  userInfoSub: Subscription;

  constructor(private authenticationService: AuthenticationService, private proposalService: ProposalService, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.proposalId = +this.route.snapshot.paramMap.get("id");
    this.createPageSubscription();
  }

  ngOnDestroy(): void {
    this.userInfoSub?.unsubscribe();
    this.proposalSub.unsubscribe();
    this.currentUserSub.unsubscribe();
  }

  onPageChange(pageEvent: PageEvent) {
    this.userInfoSub?.unsubscribe();
    this.proposalSub.unsubscribe();
    this.currentUserSub.unsubscribe();
    this.createPageSubscription();
  }

  createPageSubscription() {
    this.proposalSub = this.proposalService.getById(this.proposalId).subscribe((proposal) => {
      console.log(this.proposalId);
      console.log(proposal);
      this.proposal = proposal;
      this.currentUserSub = this.authenticationService.getCurrentUser().subscribe((currentUser)=> {
        console.log(currentUser);
        this.currentUser = currentUser;
        if(this.currentUser.role == 'ROLE_GUEST') {
          this.userInfoSub = this.proposalService.getGuestUserInfoByProposalId(this.proposalId).subscribe((userInfo) => {
            this.budget = userInfo.budget;
            console.log(userInfo.isInvited);
            this.isInvited = userInfo.isInvited;
            this.hasReplied = userInfo.hasReplied;
            console.log(userInfo);
            console.log(this.isInvited);
            console.log(this.hasReplied);
            console.log(this.budget);
          });
        }
      });
    });
  }

}
