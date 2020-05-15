import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ProfileComponent } from "./components/profile/profile.component";
import { ProposalComponent } from "./components/proposal/proposal.component";


const routes: Routes = [
  { path: '', component: HomeComponent },
  // { path: 'user/:id', component: ProfileComponent},
  { path:'login', component: LoginComponent},
  { path:'register', component: RegisterComponent},
  { path:'login', component: LoginComponent},
  { path: 'user', component: ProfileComponent},
  { path: 'proposal', component: ProposalComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
