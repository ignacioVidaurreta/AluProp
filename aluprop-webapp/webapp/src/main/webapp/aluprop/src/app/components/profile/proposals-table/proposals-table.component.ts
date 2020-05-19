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
  @Input() userProposals: UserProposal[];
  @Input() userId: number;
  @Input() userRole: string;

  dataSource = new MatTableDataSource<UserProposal>(this.userProposals);

  constructor() { }

  ngOnInit(): void {
    console.log('proposals');
    console.log(this.dataSource.data);
    this.dataSource.paginator = this.paginator;
  }
}
