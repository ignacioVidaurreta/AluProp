import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from 'src/app/modules/material.module';
import { InterestsTableComponent } from './interests-table/interests-table.component';
import { PropertiesTableComponent } from './properties-table/properties-table.component';
import { ProposalsTableComponent } from './proposals-table/proposals-table.component';
import { Routes, RouterModule } from '@angular/router';
import { ProfileComponent } from './profile.component';
import { TranslateModule } from '@ngx-translate/core';

const routes: Routes = [{path: '', component: ProfileComponent}];
const routing: ModuleWithProviders = RouterModule.forChild(routes);

@NgModule({
  declarations: [
    ProfileComponent,
    InterestsTableComponent,
    PropertiesTableComponent,
    ProposalsTableComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    TranslateModule,
    routing
  ]
})
export class ProfileModule { }
