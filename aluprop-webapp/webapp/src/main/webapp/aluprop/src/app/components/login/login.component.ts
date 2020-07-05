import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { AuthenticationService} from '../../services/authentication.service'
import { FormControl, Validators, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {

  constructor(private router: Router, 
              private authenticationService: AuthenticationService){ }

  loginForm = new FormGroup({    
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
  });

  username = this.loginForm.get('username');
  password = this.loginForm.get('password');
  ngOnInit() { }

  hasInvalidCredentials: boolean = false;;

  login() : void {
    if(this.username.valid && this.password.valid){
      this.authenticationService.login(this.loginForm.value).subscribe( (response) =>{
        if (response){
          this.hasInvalidCredentials = false;
          this.router.navigate([""]);
        } else {
          this.hasInvalidCredentials = true;
        }
      }, (error: any) => {
        this.hasInvalidCredentials = true;
      });
    }
  }

}
