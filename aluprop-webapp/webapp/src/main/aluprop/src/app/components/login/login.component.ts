import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { AuthenticationService} from '../../services/authentication.service'
import { FormControl, Validators, FormGroup } from '@angular/forms';
import { PropertyService } from 'src/app/services/property.service';
import { Role } from 'src/app/models/user';

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

  logging: boolean;
  username = this.loginForm.get('username');
  password = this.loginForm.get('password');
  ngOnInit() {
    this.logging = false;
  }

  hasInvalidCredentials: boolean = false;

  login() : void {
    if(!this.logging) {
      if(this.username.valid && this.password.valid){
        this.logging = true;
        this.authenticationService.login(this.loginForm.value).subscribe( (response) =>{
          if (response){
            this.hasInvalidCredentials = false;
            if (this.activatedRoute.snapshot.queryParams.returnProperty && response.body.role === Role.Guest){
              this.propertyService.markInterest(this.activatedRoute.snapshot.queryParams.returnProperty).subscribe(
                (response) => {
                  this.router.navigate(['property/'+ this.activatedRoute.snapshot.queryParams.returnProperty]);
                });
            } else if (this.activatedRoute.snapshot.queryParams.returnProposal){
              this.router.navigate(['proposal/'+ this.activatedRoute.snapshot.queryParams.returnProposal]);
            } else {
              this.router.navigate([""]);
            }
          } else {
            this.hasInvalidCredentials = true;
            this.logging = false;
          }
        }, (error: any) => {
          this.hasInvalidCredentials = true;
          this.logging = false;
        });
      }
    }
  }

  navigateToRegister() {
    if (this.activatedRoute.snapshot.queryParams.returnProperty){
      this.router.navigate(['register'], {queryParamsHandling: 'preserve'});
    } else {
      this.router.navigate(['register']);
    }
  }

}
