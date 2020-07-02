import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Observable, Subscription } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import {User} from "../../models/user";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

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
    this.currentUserSubscription.unsubscribe()
  }

  logout(): void{
    this.authenticationService.logout()
    this.router.navigate(["/login"])
  }

  isUserLoggedIn(){
    if( this.currentUser ){
      return true;
    }
    console.log(this.currentUser)
    return false;
  }

  setLanguage(language: string){
    this.translateService.use(language);
  }
}
