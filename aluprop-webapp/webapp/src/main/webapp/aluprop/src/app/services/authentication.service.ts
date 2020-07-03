import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {User} from "../models/user";
import {HttpClient} from "@angular/common/http";
import { map } from 'rxjs/operators';

const BASE_API_URL = 'http://localhost:8080/api/';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private authToken: string;
  // private currentUserSubject: BehaviorSubject<User>;
  // public currentUser: Observable<User>;

  constructor(private http: HttpClient){ }

  getCurrentUser(): Observable<User>{
    return this.http.get<User>(BASE_API_URL + 'user/');
  }

  login(username: string, password: string){
    let params = {username: username, password: password};
    return this.http.post<User>(BASE_API_URL + 'login/', {params: params}).pipe(map(
      (response)=>{
        console.log(response); //TODO: get token from response and store its value in authToken
        return response;
      })
    );
  }

  getAuthToken(): string{
    return this.authToken? this.authToken : 'it was packer';
  }

  logout(){
    this.authToken = null;
    this.http.get<User>(BASE_API_URL + 'logout/');
  }

}