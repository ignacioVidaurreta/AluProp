import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject, Subscription } from 'rxjs';
import {User, SignUpForm} from "../models/user";
import {HttpClient} from "@angular/common/http";
import { take, tap } from 'rxjs/operators';
import { Router } from '@angular/router';

const BASE_API_URL = 'http://localhost:8080/api/';
const LOCAL_STORAGE_AUTH_TOKEN = 'aluToken';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private authToken: string;
  private currentUserSubject= new BehaviorSubject<User>(null);
  public currentUser: Observable<User>;

  constructor(private http: HttpClient,
              private router: Router){
    if (localStorage.getItem(LOCAL_STORAGE_AUTH_TOKEN)){
      this.authToken = localStorage.getItem(LOCAL_STORAGE_AUTH_TOKEN);
    }
  }

  getCurrentUser(): Observable<User>{
    this.http.get<User>(BASE_API_URL + 'user').pipe(take(1)).subscribe(
      (user) => {
        this.currentUserSubject.next(<User>user);
      }
    );
    return this.currentUserSubject.asObservable();
  }

  signUp(signUpForm: SignUpForm){
    console.log(signUpForm);
    return this.http.post<User>(BASE_API_URL + 'auth/signup', signUpForm, {observe: 'response'}).pipe(tap(
      (response)=>{
        this.setAuthToken(response.headers.get('X-TOKEN'));
        this.currentUserSubject.next(response.body);
      })
    );
  }

  login(signUpForm: any){
    return this.http.post<User>(BASE_API_URL + 'login', signUpForm, {observe: 'response'}).pipe(tap(
      (response)=>{
        this.setAuthToken(response.headers.get('X-TOKEN'));
        this.currentUserSubject.next(response.body);
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

  logout(): Subscription {
    this.currentUserSubject.next(null);
    return this.http.post<User>(BASE_API_URL + 'auth/logout/', null)
      .subscribe(response => {
        this.setAuthToken(null);
        this.router.navigate(["/login"]);
      })
  }

}
