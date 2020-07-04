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

  ngOnInit() { }

  login() : void {
    this.authenticationService.login(this.loginForm.value).subscribe( (response) =>{
      if (response){
        this.router.navigate([""]);
      } else {
        alert('Invalid credentials'); //TODO: better error message kek
      }
    });
  }

}
