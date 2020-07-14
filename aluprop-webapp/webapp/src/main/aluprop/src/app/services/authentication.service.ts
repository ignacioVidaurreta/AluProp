import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject, Subscription } from 'rxjs';
import {User, SignUpForm} from "../models/user";
import {HttpClient} from "@angular/common/http";
import { take, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { environment } from './../../environments/environment'

const BASE_API_URL = environment.apiUrl;
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
    if (localStorage.getItem(LOCAL_STORAGE_AUTH_TOKEN) && localStorage.getItem(LOCAL_STORAGE_AUTH_TOKEN) != 'null'){
      this.authToken = localStorage.getItem(LOCAL_STORAGE_AUTH_TOKEN);
    }
  }

  isLoggedIn(): boolean{
    return this.getAuthToken() && this.getAuthToken() !== '';
  }

  getCurrentUser(): Observable<User>{
    if (this.isLoggedIn()){
      this.http.get<User>(BASE_API_URL + 'user').pipe(take(1)).subscribe(
        (user) => {
          this.currentUserSubject.next(<User>user);
        }
      );
    }
    return this.currentUserSubject.asObservable();
  }

  signUp(signUpForm: SignUpForm){
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
        this.setAuthToken('');
        this.router.navigate(["/login"]);
      })
  }

}
