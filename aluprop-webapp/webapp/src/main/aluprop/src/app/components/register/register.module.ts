import { CommonModule } from '@angular/common';
import { ModuleWithProviders, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MaterialModule } from 'src/app/modules/material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { RegisterComponent } from './register.component';

const routes: Routes = [{path: '', component: RegisterComponent}];
const routing: ModuleWithProviders = RouterModule.forChild(routes);

@NgModule({
  declarations: [RegisterComponent],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    TranslateModule,
    routing,
  ],
})
export class RegisterModule { }
