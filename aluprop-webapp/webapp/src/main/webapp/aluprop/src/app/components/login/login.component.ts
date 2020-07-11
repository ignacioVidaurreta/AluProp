import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { AuthenticationService} from '../../services/authentication.service'
import { FormControl, Validators, FormGroup } from '@angular/forms';
import { PropertyService } from 'src/app/services/property.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {

  constructor(private router: Router, 
              private activatedRoute: ActivatedRoute,
              private authenticationService: AuthenticationService,
              private propertyService: PropertyService){ }

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

          if (this.activatedRoute.snapshot.queryParams.sonuestro){
            this.propertyService.markInterest(this.activatedRoute.snapshot.queryParams.sonuestro).subscribe(
              (response) => {
                this.router.navigate(['property/'+ this.activatedRoute.snapshot.queryParams.sonuestro]);
              });
          } else {
            this.router.navigate([""]);
          }
        } else {
          this.hasInvalidCredentials = true;
        }
      }, (error: any) => {
        this.hasInvalidCredentials = true;
      });
    }
  }

  navigateToRegister() {
    if (this.activatedRoute.snapshot.queryParams.sonuestro){
      this.router.navigate(['register'], {queryParamsHandling: 'preserve'});
    } else {
      this.router.navigate(['register']);
    }
  }

}
