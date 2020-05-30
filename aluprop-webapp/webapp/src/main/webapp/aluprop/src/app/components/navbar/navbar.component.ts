import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Observable, Subscription } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  usernameSubscription: Subscription;
  username: string;
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private translateService: TranslateService) { 
  }

  ngOnInit(): void {
    this.usernameSubscription = this.authenticationService.currentUser.subscribe(
      username => this.username = username
      );
  }

  ngOnDestroy(): void{
    this.usernameSubscription.unsubscribe()
  }

  logout(): void{
    this.authenticationService.logout()
    this.router.navigate(["/login"])
  }

  isUserLoggedIn(){
    if( this.username ){
      return true;
    }
    console.log(this.username)
    return false;
  }

  setLanguage(language: string){
    this.translateService.use(language);
  }
}
