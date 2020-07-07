import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ProfileComponent } from "./components/profile/profile.component";
import { ProposalComponent } from "./components/proposal/proposal.component";
import { AuthGuardService } from "./services/auth-guard.service";
import  {DetailedPropertyComponent } from "./components/detailed-property/detailed-property.component";
import { NotificationsComponent } from './components/notifications/notifications.component';
import { CreatePropertyComponent } from './components/create-property/create-property.component';
import {ErrorComponent} from "./components/error/error.component";

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path:'login', component: LoginComponent},
  { path:'register', component: RegisterComponent},
  { path:'host/create', component: CreatePropertyComponent},
  {
    path: 'user/:id',
    component: ProfileComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'property/:id',
    component: DetailedPropertyComponent,
  },
  { path: 'proposal/:id',
    component: ProposalComponent,
    canActivate: [AuthGuardService],
  },
  { path: 'notifications',
    component: NotificationsComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'error/:code',
    component: ErrorComponent,
  },
  { path: '**', redirectTo: 'error/404' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
