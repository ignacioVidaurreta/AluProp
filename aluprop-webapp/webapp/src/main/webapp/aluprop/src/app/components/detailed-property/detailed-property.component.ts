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
  interestedUsers: User[];
  interestedUsersSub: Subscription;
  loggedInUser: boolean;

  constructor(private propertyService: PropertyService, private authenticationService: AuthenticationService, public dialog: MatDialog, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.propertyId = +this.route.snapshot.paramMap.get("id");
    console.log(this.propertyId);
    this.loggedInUser = false;
    this.createPageSubscription();
  }

  ngOnDestroy(): void {
    this.propertySub.unsubscribe();
    this.currentUserSub.unsubscribe();
    this.interestedUsersSub.unsubscribe();
  }

  onPageChange(pageEvent: PageEvent){
    this.propertySub.unsubscribe();
    this.currentUserSub.unsubscribe();
    this.interestedUsersSub.unsubscribe();
    this.createPageSubscription();
  }

  createPageSubscription(){
    this.propertySub = this.propertyService.getById(this.propertyId).subscribe((property) => {
      this.property = property;
      console.log(property);
      this.currentUserSub = this.authenticationService.getCurrentUser().subscribe((currentUser)=> {
        this.currentUser = currentUser;
        console.log(currentUser);
        this.interestedUsersSub = this.propertyService.getInterestedUsersByPropertyId(this.propertyId).subscribe((interestedUsers) => {
          console.log(this.propertyId);
          this.interestedUsers = interestedUsers;
          console.log(interestedUsers);
          if(this.currentUser) {
            this.validateIfCurrentUserIsInterested();
            this.loggedInUser = true;
          }
          console.log(this.currentUser);
          console.log(this.loggedInUser);
          console.log(this.currentUserIsInterested);
        });
      })
    });
  }

  validateIfCurrentUserIsInterested() { //TODO: does this belong in the back-end?
    let property: any;
    for(property in this.currentUser.interestedProperties) {
      console.log(property.id);
      if(property.id == this.property.id) {
        this.currentUserIsInterested = true;
        return;
      }
    }
    this.currentUserIsInterested = true;// This is hardcoded just for now until the corresponding endpoint exists
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
}
