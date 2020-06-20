import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient } from '@angular/common/http';

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
import { ProposalComponent } from './components/proposal/proposal.component';
import { ProposalUsersTableComponent } from './components/proposal/proposal-users-table/proposal-users-table.component';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import { DetailedPropertyComponent } from './components/detailed-property/detailed-property.component';
import { NotificationsMenuComponent } from './components/navbar/notifications-bar/notifications-menu/notifications-menu.component';
import { NotificationsBarComponent } from './components/navbar/notifications-bar/notifications-bar.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {MatTabsModule} from "@angular/material/tabs";
import { NotificationsComponent } from './components/notifications/notifications.component';

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
    ProposalComponent,
    ProposalUsersTableComponent,
    DetailedPropertyComponent,
    NotificationsMenuComponent,
    NotificationsBarComponent,
    NotificationsComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MaterialModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    NgbModule,
    MatTabsModule
  ],
  exports: [
    FormsModule,
    MaterialModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

// required for AOT compilation
export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}
