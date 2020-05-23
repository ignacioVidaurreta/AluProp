import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Observable, Subscription } from 'rxjs';

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
    private authenticationService: AuthenticationService) { 
  }

  ngOnInit(): void {
    this.usernameSubscription = this.authenticationService.currentUser.subscribe(
      username => this.username = username
      );
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

  ngOnDestroy(): void{
    this.usernameSubscription.unsubscribe()
  }

}
