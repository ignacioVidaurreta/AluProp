import { CommonModule } from '@angular/common';
import { NgModule, ModuleWithProviders } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PropertyGridComponent } from './property-grid/property-grid.component';
import { PropertyItemComponent } from './property-grid/property-item/property-item.component';
import { HomeComponent } from './home.component';
import { MaterialModule } from 'src/app/modules/material.module';
import { TranslateModule } from '@ngx-translate/core';

const routes: Routes = [{path: '', component: HomeComponent}];
const routing: ModuleWithProviders = RouterModule.forChild(routes);

@NgModule({
  declarations: [
    HomeComponent,
    PropertyGridComponent,
    PropertyItemComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    TranslateModule,
    routing
  ]
})
export class HomeModule { }