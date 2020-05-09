import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  currentUser: string
  constructor() { 
    this.currentUser = localStorage.getItem("currentUser");
  }

  login(username: string, password: string){
    if (username == 'admin' && password == 'admin'){
      localStorage.setItem("currentUser", username);
      this.currentUser = username;
      return true
    }else{
      return false
    }
  }

  logout(){
    localStorage.removeItem("currentUser");
  }
}
