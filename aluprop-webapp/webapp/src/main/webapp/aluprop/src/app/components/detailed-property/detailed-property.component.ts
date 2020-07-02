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
  interestedUsersSub: Subscription;
  isUserLoggedIn: boolean;
  isUserLoggedInSub: Subscription;
  languageChangedSub: Subscription;

  constructor(private propertyService: PropertyService, private userService: UserService, private translateService: TranslateService, private metadataService: MetadataService, private authenticationService: AuthenticationService, public dialog: MatDialog, private route: ActivatedRoute) {
    this.languageChangedSub = translateService.onLangChange.subscribe((newLang) => this.translateRulesAndServices());
  }

  ngOnInit(): void {
    this.propertyId = +this.route.snapshot.paramMap.get("id");
    this.createPageSubscription();
  }

  ngOnDestroy(): void {
    this.propertySub.unsubscribe();
    this.currentUserSub.unsubscribe();
    this.interestedUsersSub.unsubscribe();
    this.isUserLoggedInSub.unsubscribe();
    this.currentUserIsInterestedSub.unsubscribe();
  }

  onPageChange(pageEvent: PageEvent){
    this.propertySub.unsubscribe();
    this.currentUserSub.unsubscribe();
    this.interestedUsersSub.unsubscribe();
    this.currentUserIsInterestedSub.unsubscribe();
    this.isUserLoggedInSub.unsubscribe();
    this.createPageSubscription();
  }

  createPageSubscription(){
    this.propertySub = this.propertyService.getById(this.propertyId).subscribe((property) => {
      this.property = property;
      this.translateRulesAndServices();
      console.log(property);
      this.isUserLoggedInSub = this.userService.isUserLoggedIn().subscribe((isUserLoggedIn) => {
        this.isUserLoggedIn = isUserLoggedIn;
        if(this.isUserLoggedIn) {
          this.currentUserSub = this.authenticationService.getCurrentUser().subscribe((currentUser)=> {
            this.currentUser = currentUser;
            console.log(currentUser);
            this.interestedUsersSub = this.propertyService.getInterestedUsersByPropertyId(this.propertyId).subscribe((interestedUsers) => {
            this.interestedUsers = interestedUsers;
            });
            if(this.currentUser?.role == 'ROLE_GUEST') {
              this.currentUserIsInterestedSub = this.propertyService.isCurrentUserInterested(this.propertyId).subscribe((currentUserIsInterested) => {
                console.log(currentUserIsInterested);
                this.currentUserIsInterested = currentUserIsInterested;
              });
              }
          });
        }
      });
    });
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
      data: {interestedUsers: this.interestedUsers, property: this.property}
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  translateRulesAndServices(){
    this.metadataService.translateMetadataArray(this.property.rules);
    this.metadataService.translateMetadataArray(this.property.services);
  }
}
