import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  username: string;
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService) { 
  }

  ngOnInit(): void {
    this.username = this.authenticationService.currentUserValue;
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

}
