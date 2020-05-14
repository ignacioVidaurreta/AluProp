import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {Proposals} from "../profile.component";

@Component({
  selector: 'app-proposals-table',
  templateUrl: './proposals-table.component.html',
  styleUrls: ['./proposals-table.component.scss']
})
export class ProposalsTableComponent implements OnInit {
  displayedColumns: string[] = ['name','ownership'];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @Input() dataSource: MatTableDataSource<Proposals>;
  @Input() userId: number;
  @Input() userRole: string;

  constructor() { }

  ngOnInit(): void {
    this.dataSource.paginator = this.paginator;
  }
}
