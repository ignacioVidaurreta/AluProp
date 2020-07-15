import { CommonModule } from '@angular/common';
import { ModuleWithProviders, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MaterialModule } from 'src/app/modules/material.module';
import { TranslateModule } from '@ngx-translate/core';
import { ProposalComponent } from './proposal.component';
import { ProposalUsersTableComponent } from './proposal-users-table/proposal-users-table.component';

const routes: Routes = [{path: '', component: ProposalComponent}];
const routing: ModuleWithProviders = RouterModule.forChild(routes);

@NgModule({
  declarations: [
    ProposalComponent,
    ProposalUsersTableComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    TranslateModule,
    routing,
  ],
})
export class ProposalModule { }
