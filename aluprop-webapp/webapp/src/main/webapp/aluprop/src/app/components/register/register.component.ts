import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { AuthenticationService} from '../../services/authentication.service'


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  constructor(
    private router: Router, 
    private authenticationService: AuthenticationService
  ) { 

  }
  username: string;
  password: string;
  ngOnInit() {
  }

  login() : void {
    if(this.authenticationService.login(this.username, this.password)){
     this.router.navigate([""]);
    }else {
      alert("Invalid credentials");
    }
  }
}
