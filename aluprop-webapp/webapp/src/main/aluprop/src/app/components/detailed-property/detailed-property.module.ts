import { CommonModule } from '@angular/common';
import { ModuleWithProviders, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MaterialModule } from 'src/app/modules/material.module';
import { TranslateModule } from '@ngx-translate/core';
import { DetailedPropertyComponent } from './detailed-property.component';
import { CreateProposalModalComponent } from './create-proposal-modal/create-proposal-modal.component';
import { InterestedUsersModalComponent } from './interested-users-modal/interested-users-modal.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';

const routes: Routes = [{path: '', component: DetailedPropertyComponent}];
const routing: ModuleWithProviders = RouterModule.forChild(routes);

@NgModule({
  declarations: [
    DetailedPropertyComponent,
    CreateProposalModalComponent,
    InterestedUsersModalComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    FormsModule,
    NgbModule,
    TranslateModule,
    routing,
  ],
})
export class DetailedPropertyModule { }
