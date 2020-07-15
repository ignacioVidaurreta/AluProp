import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {Property} from "../../../models/property";
import {Subscription} from "rxjs";
import {UserService} from "../../../services/user.service";
import {ActivatedRoute} from "@angular/router";
import {UserProposal} from "../../../models/userProposal";
import {Proposal} from "../../../models/proposal";
import {PageRequest} from "../../../interfaces/page-request";

@Component({
  selector: 'app-properties-table',
  templateUrl: './properties-table.component.html',
  styleUrls: ['./properties-table.component.scss']
})
export class PropertiesTableComponent implements OnInit {
  displayedColumns: string[] = ['name', 'state'];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  totalItems: number;
  pageSize: number;

  pageRequest: PageRequest;

  ownedPropertiesSub: Subscription;
  dataSource;

  constructor(private userService: UserService, private route: ActivatedRoute) {
    this.dataSource = [];
    this.pageRequest = {pageNumber: 0, pageSize: 10}
  }

  ngOnInit(): void {
    this.dataSource.paginator = this.paginator;
    this.createPageSubscription();
  }

  ngOnDestroy(): void {
    this.ownedPropertiesSub.unsubscribe();
  }

  onPageChange(pageEvent: PageEvent){
    this.pageRequest.pageNumber = pageEvent.pageIndex;
    this.pageRequest.pageSize = pageEvent.pageSize;
    this.ownedPropertiesSub.unsubscribe();
    this.createPageSubscription();
  }

  createPageSubscription(){
    this.ownedPropertiesSub = this.userService.getAllOwnedProperties(this.pageRequest).subscribe((pageResponse) => {
      this.dataSource = new MatTableDataSource<Property>(pageResponse.responseData);
      this.totalItems = pageResponse.totalItems;
      this.pageSize = pageResponse.pageSize;
    });
  }
}
