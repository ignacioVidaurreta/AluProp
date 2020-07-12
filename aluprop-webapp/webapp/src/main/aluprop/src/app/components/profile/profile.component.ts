import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {PageRequest} from "../../interfaces/page-request";
import {PageResponse} from "../../interfaces/page-response";
import {Subscription} from "rxjs";
import {User} from "../../models/user";
import {UserService} from "../../services/user.service";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {ProposalService} from "../../services/proposal.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthenticationService} from "../../services/authentication.service";
import {filter} from "rxjs/operators";
import {MatSnackBar, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition} from "@angular/material/snack-bar";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
  providers: [MatSnackBar]
})
export class ProfileComponent implements OnInit {
  userId: number;
  user: User;
  userSub: Subscription;
  currentUser: User;
  currentUserSub: Subscription;
  deletionParamsSub: Subscription;

  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'top';

  openSnackBar() {
    this._snackBar.open('You have successfully deleted your property', 'Dismiss', {
      duration: 2000,
      horizontalPosition: this.horizontalPosition,
      verticalPosition: this.verticalPosition,
    });
  }

  constructor( private cdr: ChangeDetectorRef, 
              private _snackBar: MatSnackBar,
              private userService: UserService, 
              private route: ActivatedRoute, 
              private router: Router,
              private authenticationService: AuthenticationService) {
    this.deletionParamsSub = route.queryParams.pipe(
      filter((params) => Object.keys(params).length !== 0)
    ).subscribe((params)=>{
      if(params.deletion == 'SUCCESSFUL')
        this.openSnackBar();
      this.createPageSubscription();
    });
  }

  ngOnInit(): void {
    this.userId = +this.route.snapshot.paramMap.get("id");
    this.createPageSubscription();
  }

  ngOnDestroy(): void {
    if (this.userSub){ this.userSub.unsubscribe()}
    if (this.currentUserSub){ this.currentUserSub.unsubscribe()}
    if (this.deletionParamsSub) { this.deletionParamsSub.unsubscribe()}
  }

  onPageChange(){
    if (this.userSub){ this.userSub.unsubscribe()};
    if (this.currentUserSub){ this.currentUserSub.unsubscribe()};
    this.createPageSubscription();
  }

  createPageSubscription(){
    this.userSub = this.userService.getUserById(this.userId).subscribe((user) => {
      this.user = user;
      console.log(this.user);
      this.currentUserSub = this.authenticationService.getCurrentUser().subscribe((currentUser)=> {
        this.currentUser = currentUser;
      });
    }, (error: any) => {
      this.router.navigate(['error/404']);
    });
  }

  date(birthDate: Date) {
    return new Date(birthDate).toLocaleDateString();
  }
}
