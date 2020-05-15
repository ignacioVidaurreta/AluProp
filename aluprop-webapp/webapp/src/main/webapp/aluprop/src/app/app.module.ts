import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';

import { MaterialModule } from './modules/material.module';

import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { PropertyGridComponent } from './components/home/property-grid/property-grid.component';
import { PropertyItemComponent } from './components/home/property-grid/property-item/property-item.component';
import { RegisterComponent } from './components/register/register.component';
import { ProfileComponent } from './components/profile/profile.component';
import { PropertiesTableComponent } from './components/profile/properties-table/properties-table.component';
import { InterestsTableComponent } from './components/profile/interests-table/interests-table.component';
import { ProposalsTableComponent } from './components/profile/proposals-table/proposals-table.component';
import { SearchFiltersComponent } from './components/navbar/search-bar/search-filters/search-filters.component';
import { SearchBarComponent } from './components/navbar/search-bar/search-bar.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    LoginComponent,
    PropertyGridComponent,
    PropertyItemComponent,
    RegisterComponent,
    ProfileComponent,
    PropertiesTableComponent,
    InterestsTableComponent,
    ProposalsTableComponent,
    SearchFiltersComponent,
    SearchBarComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    MaterialModule,
  ],
  exports: [
    FormsModule,
    MaterialModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
