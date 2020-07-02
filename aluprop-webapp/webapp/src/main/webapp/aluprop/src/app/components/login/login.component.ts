import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { AuthenticationService} from '../../services/authentication.service'

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {

  constructor(
    private router: Router, 
    private authenticationService: AuthenticationService){ 
      authenticationService.login('admin', 'admin');
  }
  username: string;
  password: string;
  ngOnInit() {
  }

  login() : void {
    if(this.authenticationService.login(this.username, this.password)){
     this.router.navigate([""]);
    }else {
      alert("Invalid credentials"); // TODO: Improve error handling lol
    }
  }

}
