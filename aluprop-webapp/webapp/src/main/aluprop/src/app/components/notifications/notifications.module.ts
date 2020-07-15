import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from 'src/app/modules/material.module';
import { NotificationsComponent } from './notifications.component';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [{path: '', component: NotificationsComponent}];
const routing: ModuleWithProviders = RouterModule.forChild(routes);

@NgModule({
  declarations: [NotificationsComponent],
  imports: [
    CommonModule,
    MaterialModule,
    routing
  ]
})
export class NotificationsModule { }
