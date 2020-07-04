import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {User, SignUpForm} from "../models/user";
import {HttpClient} from "@angular/common/http";
import { map } from 'rxjs/operators';

const BASE_API_URL = 'http://localhost:8080/api/';
const LOCAL_STORAGE_AUTH_TOKEN = 'aluToken';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private authToken: string;
  // private currentUserSubject: BehaviorSubject<User>;
  // public currentUser: Observable<User>;

  constructor(private http: HttpClient){
    // localStorage.get(LOCAL_STORAGE_AUTH_TOKEN)
    if (localStorage.getItem(LOCAL_STORAGE_AUTH_TOKEN)){
      this.authToken = localStorage.getItem(LOCAL_STORAGE_AUTH_TOKEN);
    }
  }

  getCurrentUser(): Observable<User>{
    return this.http.get<User>(BASE_API_URL + 'user');
  }

  signUp(signUpForm: SignUpForm){
    console.log(signUpForm);
    return this.http.post<User>(BASE_API_URL + 'signUp', signUpForm, {observe: 'response'}).pipe(map(
      (response)=>{
        this.setAuthToken(response.headers.get('X-TOKEN'));
        return response;
      })
    );
  }

  login(signUpForm: any){
    return this.http.post<User>(BASE_API_URL + 'login', signUpForm, {observe: 'response'}).pipe(map(
      (response)=>{
        this.setAuthToken(response.headers.get('X-TOKEN'));
        return response;
      })
    );
  }

  getAuthToken(): string {
    return this.authToken;
  }

  setAuthToken(newVal: string): void {
    this.authToken = newVal;
    localStorage.setItem(LOCAL_STORAGE_AUTH_TOKEN, newVal);
  }

  logout(){
    this.setAuthToken(null);
    this.http.get<User>(BASE_API_URL + 'logout/');
  }

}