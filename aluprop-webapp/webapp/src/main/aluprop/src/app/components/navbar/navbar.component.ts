import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';
import {Observable, Subject, Subscription} from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { User, Role } from "../../models/user";
import { take } from 'rxjs/operators';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  roles = Role;

  currentUserSubscription: Subscription;
  currentUser: User;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private translateService: TranslateService) {
  }

  ngOnInit(): void {
    this.currentUserSubscription = this.authenticationService.getCurrentUser().subscribe((currentUser)=> {
      this.currentUser = currentUser;
    });
  }

  ngOnDestroy(): void{
    this.currentUserSubscription.unsubscribe();
  }

  logout(): void{
    this.authenticationService.logout();
  }

  isUserLoggedIn(){
    if( this.currentUser ){
      return true;
    }
    return false;
  }

  setLanguage(language: string){
    this.translateService.use(language);
  }
}
