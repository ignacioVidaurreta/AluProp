import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {Property} from "../../../models/property";


@Component({
  selector: 'app-interests-table',
  templateUrl: './interests-table.component.html',
  styleUrls: ['./interests-table.component.scss']
})
export class InterestsTableComponent implements OnInit {
  displayedColumns: string[] = ['name'];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @Input() interestedProperties: Property[];

  dataSource;

  constructor() {
    this.dataSource = [];
  }

  ngOnInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource = new MatTableDataSource<Property>(this.interestedProperties);
  }
}
