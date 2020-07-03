import { Injectable } from '@angular/core';
import { Router, CanActivate } from '@angular/router';
import { AuthenticationService } from './authentication.service'
@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(public auth: AuthenticationService, public router: Router){}

  canActivate(): boolean {
    if(this.auth.getAuthToken() !== ''){
      console.log("Hola");
      this.router.navigate(['login']);
      return false;
    }
    return true;
  }

}
