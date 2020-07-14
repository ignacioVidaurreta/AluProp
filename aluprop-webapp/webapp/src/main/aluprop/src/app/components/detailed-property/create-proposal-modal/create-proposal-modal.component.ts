import {AfterViewInit, Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import * as moment from "moment";
import {User} from "../../../models/user";
import {Property} from "../../../models/property";
import {MatSelectionList, MatSelectionListChange} from "@angular/material/list";
import {Subscription} from "rxjs";
import {ProposalService} from "../../../services/proposal.service";
import { Router } from '@angular/router';

export interface DialogData {
  interestedUsers: User[];
  currentUser: User;
  property: Property;
}

@Component({
  selector: 'app-create-proposal-modal',
  templateUrl: './create-proposal-modal.component.html',
  styleUrls: ['./create-proposal-modal.component.scss']
})
export class CreateProposalModalComponent implements OnInit, AfterViewInit {
  selectedOptions = [];
  selectedOption;
  showErrorMaxGuests: boolean;
  @ViewChild(MatSelectionList) users: MatSelectionList;
  createProposalSub: Subscription;

  ngOnInit(): void {
    this.showErrorMaxGuests = false;
  }

  ngAfterViewInit(): void {
    this.users.selectionChange.subscribe((s: MatSelectionListChange) => {
      //this.users.deselectAll();
      if(this.users.selectedOptions.selected.length > (this.data.property.capacity - 1)) {
        s.option.selected = false;
        this.showErrorMaxGuests = true;
      }
      else {
        this.showErrorMaxGuests = false;
      }
    });
  }

  constructor(public dialogRef: MatDialogRef<CreateProposalModalComponent>, 
              @Inject(MAT_DIALOG_DATA) public data: DialogData, 
              private proposalService: ProposalService,
              private router: Router) { }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onClick(): void {
  }

  createProposal() {
    let invitedUsersIds = this.selectedOptions.map((user) => user.id);
    this.createProposalSub = this.proposalService.createProposal(invitedUsersIds, this.data.property.id).subscribe(
      (proposal) => {
        this.router.navigate(['proposal/' + proposal.id]);
      });
  }

  onNgModelChange($event){
    this.selectedOption=$event;
  }

  ageFromDateOfBirthday(dateOfBirth: any): number {
    return moment().diff(dateOfBirth, 'years');
  }

  getPricePerPerson(numberOfPeople: number): number {
    return this.data.property.price / numberOfPeople;
  }

}
