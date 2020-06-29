import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import * as moment from "moment";
import {User} from "../../../models/user";
import {Property} from "../../../models/property";

export interface DialogData {
  interestedUsers: User[];
  property: Property;
}

@Component({
  selector: 'app-create-proposal-modal',
  templateUrl: './create-proposal-modal.component.html',
  styleUrls: ['./create-proposal-modal.component.scss']
})
export class CreateProposalModalComponent implements OnInit {

  ngOnInit(): void {}

  constructor(public dialogRef: MatDialogRef<CreateProposalModalComponent>, @Inject(MAT_DIALOG_DATA) public data: DialogData) { }

  onNoClick(): void {
    this.dialogRef.close();
  }

  ageFromDateOfBirthday(dateOfBirth: any): number {
    return moment().diff(dateOfBirth, 'years');
  }


}
