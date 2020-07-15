import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuardService } from "./services/auth-guard.service";
import {ErrorComponent} from "./components/error/error.component";

const routes: Routes = [
  { path: '', 
    loadChildren: () => import('./components/home/home.module').then(mod => mod.HomeModule)},
  { path:'login', 
    loadChildren: () => import('./components/login/login.module').then(mod => mod.LoginModule)
  },
  { path:'register', 
    loadChildren: () => import('./components/register/register.module').then(mod => mod.RegisterModule)
  },
  { path:'host/create', 
    loadChildren: () => import('./components/create-property/create-property.module').then(mod => mod.CreatePropertyModule)
  },
  {
    path: 'user/:id',
    canActivate: [AuthGuardService],
    loadChildren: () => import('./components/profile/profile.module').then(mod => mod.ProfileModule)
  },
  {
    path: 'property/:id',
    loadChildren: () =>import('./components/detailed-property/detailed-property.module').then(mod => mod.DetailedPropertyModule)
  },
  { path: 'proposal/:id',
    canActivate: [AuthGuardService],
    loadChildren: () => import('./components/proposal/proposal.module').then(mod => mod.ProposalModule)
  },
  { path: 'notifications',
    canActivate: [AuthGuardService],
    loadChildren: () => import('./components/notifications/notifications.module').then(mod => mod.NotificationsModule)
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
