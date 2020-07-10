import { Component, OnInit } from '@angular/core';
import {ProposalService} from "../../services/proposal.service";
import {PageEvent} from "@angular/material/paginator";
import {Subject, Subscription} from "rxjs";
import {Proposal} from "../../models/proposal";
import { ActivatedRoute } from "@angular/router";
import {UserProposal} from "../../models/userProposal";
import {User} from "../../models/user";
import {UserService} from "../../services/user.service";
import {AuthenticationService} from "../../services/authentication.service";
import {take} from "rxjs/operators";
import {ImageService} from "../../services/image.service";
import {DomSanitizer} from "@angular/platform-browser";

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
  reloadUsersTable: Subject<void> = new Subject<void>();

  constructor(private authenticationService: AuthenticationService,
              private proposalService: ProposalService,
              private route: ActivatedRoute,
              private imageService: ImageService,
              private _sanitizer: DomSanitizer) {
  }

  ngOnInit(): void {
    this.proposalId = +this.route.snapshot.paramMap.get("id");
    this.createPageSubscription();
  }

  ngOnDestroy(): void {
    this.userInfoSub?.unsubscribe();
    this.proposalSub?.unsubscribe();
    this.currentUserSub?.unsubscribe();
  }

  onPageChange() {
    this.userInfoSub?.unsubscribe();
    this.proposalSub?.unsubscribe();
    this.currentUserSub?.unsubscribe();
    this.createPageSubscription();
  }

  createPageSubscription() {
    this.proposalSub = this.proposalService.getById(this.proposalId).subscribe((proposal) => {
      console.log(this.proposalId);
      console.log(proposal);
      this.proposal = proposal;
      this.fetchPropertyImage();
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

  fetchPropertyImage() {
    this.imageService.getImage(this.proposal.property.mainImage.id).subscribe(imageData => {
      this.proposal.property.mainImage.image = this._sanitizer.bypassSecurityTrustResourceUrl(imageData);
    });
  }

  acceptProposal() {
    if (this.currentUser.role == 'ROLE_GUEST') {
      this.proposalService.acceptProposalGuest(this.proposal.id).pipe(take(1)).subscribe(() => {
        this.onPageChange();
        this.reloadUsersTable.next()
      });
    } else if(this.currentUser.role == 'ROLE_HOST') {
      this.proposalService.acceptProposalHost(this.proposal.id).pipe(take(1)).subscribe(() => {
        this.onPageChange();
        this.reloadUsersTable.next()
      });
    }
  }

  declineProposal() {
    if (this.currentUser.role == 'ROLE_GUEST') {
      this.proposalService.declineProposalGuest(this.proposal.id).pipe(take(1)).subscribe(() => {
        this.onPageChange();
        this.reloadUsersTable.next()
      });
    } else if(this.currentUser.role == 'ROLE_HOST') {
      this.proposalService.declineProposalHost(this.proposal.id).pipe(take(1)).subscribe(() => {
        this.onPageChange();
        this.reloadUsersTable.next()
      });
    }
  }

  cancelProposal() {
    this.proposalService.dropProposal(this.proposal.id).pipe(take(1)).subscribe(() => {
      this.onPageChange();
      this.reloadUsersTable.next()
    });
  }

}
