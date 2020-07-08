import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { AuthenticationService} from '../../services/authentication.service'
import { Validators, FormControl, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  signUpForm = new FormGroup({    
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
    repeatPassword: new FormControl('', [Validators.required]),
    firstName: new FormControl('', [Validators.required]),
    lastName: new FormControl('', [Validators.required]),
    phoneNumber: new FormControl('', [Validators.required]),
    birthday: new FormControl('', [Validators.required]),
    privacy: new FormControl('', [Validators.required]),
    bio: new FormControl('', [Validators.required]),
    gender: new FormControl('', [Validators.required])
  },
    {validators: this.checkIfMatchingPasswords('password', 'repeatPassword')}
  );
  formChangesSub: Subscription;

  passwordMismatch: boolean;
  
  constructor(
    private authenticationService: AuthenticationService
  ) { 
    this.formChangesSub = this.signUpForm.valueChanges.subscribe((filters) => {
      this.onPhoneChange(filters['phoneNumber']);
    });
  }
  username: string;
  password: string;
  ngOnInit() { }

  ngOnDestroy() {
    if (this.formChangesSub) { this.formChangesSub.unsubscribe()}
  }

  onPhoneChange(phone: string) {
    let phoneForm = phone || '';

    // Only allow characters that have sense in a phone number
    phoneForm = phoneForm.replace(/[^0-9()+-]/gi, '');

    const actualLength = this.getActualLength(phoneForm);
    if (actualLength === 5) {
      phoneForm = this.transformIntoHomePhone(phoneForm);
    }
    if (actualLength === 9 || actualLength === 10) {
      phoneForm = this.transformIntoCellPhone(phoneForm);
    }
    if (actualLength >= 11) {
      const numbersInAreaCode = actualLength - 10;
      phoneForm = this.includeAreaCode(phoneForm, numbersInAreaCode);
    }

    if (this.signUpForm.get('phoneNumber').value !== phoneForm){
      this.signUpForm.get('phoneNumber').setValue(phoneForm);
    }
  }

  removePreviousFormats(phone: string): string {
    return phone.replace(/[- ()]/g, '');
  }
  transformIntoHomePhone(phone: string): string {
    const auxPhone = this.removePreviousFormats(phone);

    return auxPhone.substring(0, 4) + '-' + auxPhone.substring(4);
  }

  transformIntoCellPhone(phone: string): string {
    const auxPhone = this.removePreviousFormats(phone);

    return auxPhone.substring(0, 2) + ' ' + auxPhone.substring(2, 6) + '-' + auxPhone.substring(6);
  }

  includeAreaCode(phone: string, numbersInAreaCode: number): string {
    const auxPhone = this.removePreviousFormats(phone);

    return '(' + auxPhone.substring(0, numbersInAreaCode) + ') '
      + auxPhone.substring(numbersInAreaCode, numbersInAreaCode + 2) + ' '
      + auxPhone.substring(numbersInAreaCode + 2, numbersInAreaCode + 6) + '-'
      + auxPhone.substring(numbersInAreaCode + 6);
  }

  getActualLength(formattedPhone: string): number {
    return this.removePreviousFormats(formattedPhone).length;
  }

  signUp() {
    if(this.signUpForm.valid)
      this.authenticationService.signUp(this.signUpForm.value).subscribe(() => console.log('holaaa'));
  }


  private checkIfMatchingPasswords(passwordKey: string, passwordConfirmationKey: string) {
    return (group: FormGroup) => {
      const passwordInput = group.controls[passwordKey],
        passwordConfirmationInput = group.controls[passwordConfirmationKey];
      if (passwordInput.value !== passwordConfirmationInput.value) {
        passwordConfirmationInput.setErrors({notEquivalent: true});
        return {notEquivalent: true};
      } else {
        passwordConfirmationInput.setErrors(null);
      }
    };
  }

}
