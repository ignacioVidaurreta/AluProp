import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {PropertyService} from "../../services/property.service";
import {PageEvent} from "@angular/material/paginator";
import {Subscription} from "rxjs";
import {Property} from "../../models/property";
import {User} from "../../models/user";
import {AuthenticationService} from "../../services/authentication.service";
import {MatDialog} from "@angular/material/dialog";
import {InterestedUsersModalComponent} from "./interested-users-modal/interested-users-modal.component";
import {CreateProposalModalComponent} from "./create-proposal-modal/create-proposal-modal.component";
import {UserService} from "../../services/user.service";
import {MetadataService} from "../../metadata.service";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-detailed-property',
  templateUrl: './detailed-property.component.html',
  styleUrls: ['./detailed-property.component.scss']
})
export class DetailedPropertyComponent implements OnInit {

  propertyId: number;
  property: Property;
  propertySub: Subscription;
  currentUser: User;
  currentUserSub: Subscription;
  currentUserIsInterested: boolean;
  currentUserIsInterestedSub: Subscription;
  interestedUsers: User[];
  interestedUsersWithoutCurrentUser: User[];
  interestedUsersSub: Subscription;
  languageChangedSub: Subscription;
  changePropertyStatusSub: Subscription;
  interestSub: Subscription;
  uninterestSub: Subscription;
  userIsloogedIn: boolean;

  constructor(private propertyService: PropertyService, private userService: UserService, private translateService: TranslateService, private metadataService: MetadataService, private authenticationService: AuthenticationService, public dialog: MatDialog, private route: ActivatedRoute) {
    this.languageChangedSub = translateService.onLangChange.subscribe((newLang) => this.translateRulesAndServices());
  }

  ngOnInit(): void {
    this.propertyId = +this.route.snapshot.paramMap.get("id");
    this.createPageSubscription();
  }

  ngOnDestroy(): void {
    this.dropSubscriptions();
  }

  onPageChange() {
    this.dropSubscriptions();
    this.createPageSubscription();
  }

  dropSubscriptions() {
    if (this.propertySub){ this.propertySub.unsubscribe()};
    if (this.currentUserSub){ this.currentUserSub.unsubscribe() };
    if (this.interestedUsersSub){ this.interestedUsersSub.unsubscribe() };
    if (this.currentUserIsInterestedSub){ this.currentUserIsInterestedSub.unsubscribe() };
    if (this.changePropertyStatusSub){ this.changePropertyStatusSub.unsubscribe() }
    if (this.interestSub){ this.interestSub.unsubscribe() }
    if (this.uninterestSub){ this.uninterestSub.unsubscribe() }
  }

  createPageSubscription() {
    this.propertySub = this.propertyService.getById(this.propertyId).subscribe((property) => {
      this.property = property;
      this.translateRulesAndServices();
      this.currentUserSub = this.authenticationService.getCurrentUser().subscribe((currentUser)=> {
        this.currentUser = currentUser;
        this.userIsloogedIn = this.isUserLoggedIn();
        this.interestedUsersSub = this.propertyService.getInterestedUsersByPropertyId(this.propertyId).subscribe((interestedUsers) => {
        this.interestedUsers = interestedUsers;
        var index = interestedUsers.map(function(user) { return user?.id; }).indexOf(this.currentUser?.id);
        this.interestedUsersWithoutCurrentUser = interestedUsers;
        });
        if(this.currentUser?.role == 'ROLE_GUEST') {
          this.currentUserIsInterestedSub = this.propertyService.isCurrentUserInterested(this.propertyId).subscribe((currentUserIsInterested) => {
            console.log(currentUserIsInterested);
            this.currentUserIsInterested = currentUserIsInterested;
          });
          }
      });
      });
  }

  isUserLoggedIn(){
    if( this.currentUser ){
      return true;
    }
    console.log(this.currentUser)
    return false;
  }

  openDialogInterestedUsers(): void {
    const dialogRef = this.dialog.open(InterestedUsersModalComponent, {
      width: '400px',
      data: {interestedUsers: this.interestedUsers}
    });

  dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  openDialogCreateProposal(): void {
    const dialogRef = this.dialog.open(CreateProposalModalComponent, {
      width: '500px',
      data: {interestedUsers: this.interestedUsersWithoutCurrentUser, currentUser: this.currentUser, property: this.property}
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  translateRulesAndServices(){
    this.metadataService.translateMetadataArray(this.property.rules);
    this.metadataService.translateMetadataArray(this.property.services);
  }

  changePropertyAvailability() {
    console.log(this.property.availability);
    this.changePropertyStatusSub = this.propertyService.changePropertyAvailability(this.property.id).subscribe(
      () => {this.onPageChange();}
    );
  }

  markUninterest() {
    console.log(this.currentUserIsInterested);
    this.uninterestSub = this.propertyService.markUninterest(this.property.id).subscribe(
      () => {this.onPageChange();}
    );
  }

  markInterest() {
    console.log(this.currentUserIsInterested);
    this.interestSub = this.propertyService.markInterest(this.property.id).subscribe(
      () => {this.onPageChange();}
    );
  }
}
