import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HttpClient, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

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
import { DetailedPropertyComponent } from './components/detailed-property/detailed-property.component';
import { NotificationsMenuComponent } from './components/navbar/notifications-bar/notifications-menu/notifications-menu.component';
import { NotificationsBarComponent } from './components/navbar/notifications-bar/notifications-bar.component';
import { NotificationsComponent } from './components/notifications/notifications.component';
import { CreatePropertyComponent } from './components/create-property/create-property.component';
import { InterestedUsersModalComponent } from './components/detailed-property/interested-users-modal/interested-users-modal.component';
import {MatDialogModule} from "@angular/material/dialog";
import {MatTabsModule} from "@angular/material/tabs";
import { CreateProposalModalComponent } from './components/detailed-property/create-proposal-modal/create-proposal-modal.component';
import { TokenInterceptor } from './services/token-interceptor';
import { ErrorComponent } from './components/error/error.component';
import {MatBadgeModule} from "@angular/material/badge";
import {MatChipsModule} from "@angular/material/chips";
import {MatProgressBarModule} from "@angular/material/progress-bar";

@NgModule({
  schemas: [ CUSTOM_ELEMENTS_SCHEMA],
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
    InterestedUsersModalComponent,
    CreatePropertyComponent,
    CreateProposalModalComponent,
    ErrorComponent,
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
    MatTabsModule,
    MatDialogModule,
    MatBadgeModule,
    MatChipsModule,
    MatProgressBarModule
  ],
  exports: [
    FormsModule,
    MaterialModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

// required for AOT compilation
export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}
