import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {Proposal} from "../../../models/proposal";
import {UserProposal} from "../../../models/userProposal";


@Component({
  selector: 'app-proposals-table',
  templateUrl: './proposals-table.component.html',
  styleUrls: ['./proposals-table.component.scss']
})
export class ProposalsTableComponent implements OnInit {
  displayedColumns: string[] = ['name','ownership'];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @Input() proposals: UserProposal[] | Proposal[];
  @Input() userId: number;
  @Input() userRole: string;

  dataSource;

  constructor() {
    this.proposals = [];
    this.dataSource = [];
  }

  ngOnInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource = new MatTableDataSource<UserProposal|Proposal>(this.proposals);
  }
}
