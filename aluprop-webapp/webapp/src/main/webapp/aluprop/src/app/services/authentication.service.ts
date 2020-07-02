import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';
import {User} from "../models/user";
import {HttpClient} from "@angular/common/http";

const BASE_API_URL = 'http://localhost:8080/api/';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private authToken: string;
  private currentUserSubject: BehaviorSubject<User>;
  public currentUser: Observable<User>;

  constructor(private http: HttpClient){
    
  }


  getCurrentUser(): Observable<User>{
    return this.http.get<User>(BASE_API_URL + 'user/');
  }

  login(username: string, password: string){
    let params = {username: username, password: password};
    return this.http.post<User>(BASE_API_URL + 'login/', {params: params}).subscribe(
      (response)=>{
        console.log(response);
      }
    );
  }

  logout(){
    this.currentUserSubject.next(null);
  }

  public isAuthenticated(): boolean {
    return this.authToken !== null && this.authToken != '';
  }
}
