import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';
import {User} from "../models/user";
import {HttpClient} from "@angular/common/http";

const BASE_API_URL_CURRENT_USER = 'http://localhost:8080/api/user';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private currentUserSubject: BehaviorSubject<string>;
  public currentUser: Observable<string>;
  constructor(private http: HttpClient){//private jwtHelper: JwtHelperService) {
    this.currentUserSubject = new BehaviorSubject(localStorage.getItem("currentUser"));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): string {
    return this.currentUserSubject.value;
  }

  getCurrentUser(): Observable<User>{
    let header = {cookie: 'JSESSIONID=A429BB961EA8559C07AD3D61AD0EF7A2'};
    return this.http.get<User>(BASE_API_URL_CURRENT_USER, {headers: header});
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
