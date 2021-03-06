import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from 'src/app/modules/material.module';
import { NotificationsComponent } from './notifications.component';
import { RouterModule, Routes } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

const routes: Routes = [{path: '', component: NotificationsComponent}];
const routing: ModuleWithProviders = RouterModule.forChild(routes);

@NgModule({
  declarations: [NotificationsComponent],
  imports: [
    CommonModule,
    MaterialModule,
    TranslateModule,
    routing
  ]
})
export class NotificationsModule { }
