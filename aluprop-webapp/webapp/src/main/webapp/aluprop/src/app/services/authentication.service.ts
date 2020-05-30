import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private currentUserSubject: BehaviorSubject<string>;
  public currentUser: Observable<string>;
  constructor(){//private jwtHelper: JwtHelperService) { 
    this.currentUserSubject = new BehaviorSubject(localStorage.getItem("currentUser"));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): string {
    return this.currentUserSubject.value;
  }

  login(username: string, password: string){
    if (username == 'admin' && password == 'admin'){
      localStorage.setItem("currentUser", username);
      this.currentUserSubject.next(username);
      return true
    }else{
      return false
    }
  }

  logout(){
    localStorage.removeItem("currentUser");
    this.currentUserSubject.next(null);
  }

  public isAuthenticated(): boolean {
    
    let token = localStorage.getItem("currentUser");
    return  token != undefined;
    //const token = localStorage.getItem('token');



    // Check wether the token is expired and return
    // true or false
    //return !this.jwtHelper.isTokenExpired(token)
  }
}
