import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor() { }

  login(username: string, password: string){
    if (username == 'admin' && password == 'admin'){
      return true
    }else{
      return false
    }
  }
}
