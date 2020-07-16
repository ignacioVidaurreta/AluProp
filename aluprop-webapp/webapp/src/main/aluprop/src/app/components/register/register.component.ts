import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { AuthenticationService} from '../../services/authentication.service'
import { Validators, FormControl, FormGroup, ValidatorFn } from '@angular/forms';
import { Subscription } from 'rxjs';
import {User, SignUpForm, Role} from "../../models/user";
import { MetadataService } from 'src/app/metadata.service';
import { University } from 'src/app/models/university';
import { Career } from 'src/app/models/career';
import { PropertyService } from 'src/app/services/property.service';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  roleTranslator = {
    "ROLE_GUEST": "0",
    "ROLE_HOST": "1",
  };

  signUpForm = new FormGroup({    
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
    repeatPassword: new FormControl('', [Validators.required]),
    name: new FormControl('', [Validators.required]),
    lastName: new FormControl('', [Validators.required]),
    contactNumber: new FormControl('', [Validators.required, Validators.minLength(5)]),
    birthdate: new FormControl('', [Validators.required, this.dateValidator(13, 105)]),
    role: new FormControl('', [Validators.required]),
    bio: new FormControl('', [Validators.required]),
    gender: new FormControl('', [Validators.required]),
    university: new FormControl(''),
    career: new FormControl('')
  },
    {
      validators: 
      [
        this.checkIfMatchingPasswords('password', 'repeatPassword'),
        this.checkRequiredUniversity('university', 'role'),
        this.checkRequiredCareer('career', 'role')
      ]
    }
  );
  formChangesSub: Subscription;

  passwordMismatch: boolean;
  universitySub: Subscription;
  careersSub: Subscription;
  universities: University[];
  careers: Career[];
  createdUser: SignUpForm;
  isGuest: boolean;
  repeatedEmail = false;

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private metadataService: MetadataService,
    private propertyService: PropertyService
  ) { 
    this.createdUser = new SignUpForm();
    this.formChangesSub = this.signUpForm.valueChanges.subscribe((filters) => {
      this.onPhoneChange(filters['contactNumber']);
      this.isGuest = filters["role"] === Role.Guest;
      
      this.repeatedEmail = this.createdUser.email && this.createdUser.email === filters['email']
      if (this.repeatedEmail)
        this.signUpForm.controls["email"].setErrors({'invalid': true});
    });
  }
  username: string;
  password: string;
  ngOnInit(): void {
    this.universitySub = this.metadataService.getAllUniversities().subscribe((universities) => {
      this.universities = universities;
    })
    this.careersSub = this.metadataService.getAllCareers().subscribe((careers) => {
      this.careers = careers;
    })
  }

  ngOnDestroy() {
    if (this.formChangesSub) { this.formChangesSub.unsubscribe()}
    if (this.universitySub) { this.universitySub.unsubscribe()}
    if (this.careersSub) { this.careersSub.unsubscribe()}
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

    if (this.signUpForm.get('contactNumber').value !== phoneForm){
      this.signUpForm.get('contactNumber').setValue(phoneForm);
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
    if(this.signUpForm.valid){
      this.generateUserFromForm();
      this.authenticationService.signUp(this.createdUser).subscribe((response) =>{
        this.repeatedEmail = false;
        if (response){
          if (this.activatedRoute.snapshot.queryParams.sonuestro && response.body.role === Role.Guest){
            this.propertyService.markInterest(this.activatedRoute.snapshot.queryParams.sonuestro).subscribe(
              (response) => {
                this.router.navigate(['property/'+ this.activatedRoute.snapshot.queryParams.sonuestro]);
              });
          } else {
            this.router.navigate([""]);
          }
        } else {
        }
      }, (error: any) => {
        this.repeatedEmail = true;
        this.signUpForm.controls["email"].setErrors({'invalid': true});
      });
    }
  }

  generateUserFromForm(): void {
    this.createdUser.email = this.signUpForm.get("email").value;
    this.createdUser.name = this.signUpForm.get("name").value;
    this.createdUser.lastName = this.signUpForm.get("lastName").value;
    this.createdUser.password = this.signUpForm.get("password").value;
    this.createdUser.birthDate = this.signUpForm.value["birthdate"]
    this.createdUser.gender = this.signUpForm.value["gender"]
    this.createdUser.bio = this.signUpForm.value["bio"];
    this.createdUser.contactNumber = this.signUpForm.value["contactNumber"];
    if(this.signUpForm.value["role"] == Role.Guest) {
      this.createdUser.universityId = this.signUpForm.value["university"].id;
      this.createdUser.careerId = this.signUpForm.value["career"].id;
    }
    this.createdUser.role = this.roleTranslator[this.signUpForm.value["role"]];

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

  private checkRequiredUniversity(universityKey: string, roleKey: string) {
    return (group: FormGroup) => {
      const universityInput = group.controls[universityKey],
        roleInput = group.controls[roleKey];
      if (roleInput.value === Role.Guest && !universityInput.value) {
        universityInput.setErrors({invalid: true});
        return {invalid: true};
      } else {
        universityInput.setErrors(null);
      }
    };
  }

  private checkRequiredCareer(careerKey: string, roleKey: string) {
    return (group: FormGroup) => {
      const careerInput = group.controls[careerKey],
        roleInput = group.controls[roleKey];
      if (roleInput.value === Role.Guest && !careerInput.value) {
        careerInput.setErrors({invalid: true});
        return {invalid: true};
      } else {
        careerInput.setErrors(null);
      }
    };
  }

  dateValidator(minAge: number, maxAge: number): ValidatorFn{
    return control => {
      const inputYear = new Date(control.value).getFullYear();
      const currentYear = new Date().getFullYear(); //lol
      if (currentYear - inputYear < minAge){
        return {age: 'young'};
      } else if (currentYear - inputYear > maxAge){
        return {age: 'old'};
      }
      return null;
    }
  } 

  navigateToLogIn() {
    if (this.activatedRoute.snapshot.queryParams.sonuestro){
      this.router.navigate(['login'], {queryParamsHandling: 'preserve'});
    } else {
      this.router.navigate(['login']);
    }
  }
}
