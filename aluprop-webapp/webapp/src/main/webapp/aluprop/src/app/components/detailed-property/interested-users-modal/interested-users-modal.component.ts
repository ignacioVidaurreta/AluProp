import {Component, Inject, Input, OnInit} from '@angular/core';
import {User} from "../../../models/user";
import * as moment from "moment";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

export interface DialogData {
  interestedUsers: User[];
}

@Component({
  selector: 'app-interested-users-modal',
  templateUrl: './interested-users-modal.component.html',
  styleUrls: ['./interested-users-modal.component.scss']
})
export class InterestedUsersModalComponent implements OnInit {

  ngOnInit(): void {}

  constructor(public dialogRef: MatDialogRef<InterestedUsersModalComponent>, @Inject(MAT_DIALOG_DATA) public data: DialogData) { }

  onNoClick(): void {
    this.dialogRef.close();
  }

  ageFromDateOfBirthday(dateOfBirth: any): number {
    return moment().diff(dateOfBirth, 'years');
  }

}
