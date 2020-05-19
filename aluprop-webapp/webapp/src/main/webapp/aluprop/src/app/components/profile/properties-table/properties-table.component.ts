import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {Property} from "../../../models/property";

@Component({
  selector: 'app-properties-table',
  templateUrl: './properties-table.component.html',
  styleUrls: ['./properties-table.component.scss']
})
export class PropertiesTableComponent implements OnInit {
  displayedColumns: string[] = ['name', 'state'];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @Input() ownedProperties: Property[];

  dataSource;

  constructor() {
    this.dataSource = [];
  }

  ngOnInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource = new MatTableDataSource<Property>(this.ownedProperties);
  }

}
