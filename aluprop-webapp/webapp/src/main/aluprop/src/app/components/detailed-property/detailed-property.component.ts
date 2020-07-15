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
import { Router } from '@angular/router';
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";
import {ImageService} from "../../services/image.service";
import {DomSanitizer} from "@angular/platform-browser";


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
  deleteSub: Subscription;

  constructor( private propertyService: PropertyService,
               private router: Router,
               private userService: UserService,
               private translateService: TranslateService,
               private metadataService: MetadataService,
               private authenticationService: AuthenticationService,
               public dialog: MatDialog,
               private route: ActivatedRoute,
               private imageService: ImageService,
               private _sanitizer: DomSanitizer) {
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
    if (this.deleteSub){ this.deleteSub.unsubscribe() }
  }

  createPageSubscription() {
    this.propertySub = this.propertyService.getById(this.propertyId).subscribe((property) => {
      this.property = property;
      this.translateRulesAndServices();
      this.fetchPropertyImages();
      this.currentUserSub = this.authenticationService.getCurrentUser().subscribe((currentUser)=> {
        this.currentUser = currentUser;
        this.userIsloogedIn = this.isUserLoggedIn();
        this.interestedUsersSub = this.propertyService.getInterestedUsersByPropertyId(this.propertyId).subscribe((interestedUsers) => {
        this.interestedUsers = JSON.parse(JSON.stringify(interestedUsers));
        var index = interestedUsers.map(function(user) { return user?.id; }).indexOf(this.currentUser?.id);
        interestedUsers.splice(index,1);
        this.interestedUsersWithoutCurrentUser = interestedUsers;
        });
        if(this.currentUser?.role == 'ROLE_GUEST') {
          this.currentUserIsInterestedSub = this.propertyService.isCurrentUserInterested(this.propertyId).subscribe((currentUserIsInterested) => {
            this.currentUserIsInterested = currentUserIsInterested;
          });
          }
      });
      });
  }

  fetchPropertyImages() {
    this.property.images.forEach(
      (image) => {
        this.imageService.getImage(image.id).subscribe(imageData => {
          image.image = this._sanitizer.bypassSecurityTrustResourceUrl(imageData);
        });
      }
    );
  }

  isUserLoggedIn(){
    if( this.currentUser ){
      return true;
    }
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
    this.changePropertyStatusSub = this.propertyService.changePropertyAvailability(this.property.id).subscribe(
      () => {this.onPageChange();}
    );
  }

  markUninterest() {
    this.uninterestSub = this.propertyService.markUninterest(this.property.id).subscribe(
      () => {this.onPageChange();}
    );
  }

  markInterest() {
    this.interestSub = this.propertyService.markInterest(this.property.id).subscribe(
      () => {this.onPageChange();}
    );
  }

  deleteProperty() {
    this.deleteSub = this.propertyService.deleteProperty(this.property.id).subscribe(
      (response) => {
        this.router.navigate(['/user/' + this.currentUser?.id ], { queryParams: {'deletion': "SUCCESSFUL"}});
      }
    );
  }

  navigatToLogLinWithInterestIntention() {
    this.router.navigate(['login'], { queryParams: {'sonuestro': this.propertyId}});
  }
}
