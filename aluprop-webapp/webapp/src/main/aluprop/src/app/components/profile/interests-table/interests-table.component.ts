import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {Property} from "../../../models/property";
import {Subscription} from "rxjs";
import {UserService} from "../../../services/user.service";
import {ActivatedRoute} from "@angular/router";
import {PageRequest} from "../../../interfaces/page-request";


@Component({
  selector: 'app-interests-table',
  templateUrl: './interests-table.component.html',
  styleUrls: ['./interests-table.component.scss']
})
export class InterestsTableComponent implements OnInit {
  displayedColumns: string[] = ['name'];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  totalItems: number;
  pageSize: number;

  pageRequest: PageRequest;
  interestedPropertiesSub: Subscription;
  dataSource;

  responseData: Property[];

  constructor(private userService: UserService, private route: ActivatedRoute) {
    this.dataSource = [];
    this.pageRequest = {pageNumber: 0, pageSize: 10}
  }

  ngOnInit(): void {
    this.dataSource.paginator = this.paginator;
    this.createPageSubscription();
  }

  ngOnDestroy(): void {
    this.interestedPropertiesSub.unsubscribe();
  }

  onPageChange(pageEvent: PageEvent){
    this.pageRequest.pageNumber = pageEvent.pageIndex;
    this.pageRequest.pageSize = pageEvent.pageSize;
    this.interestedPropertiesSub.unsubscribe();
    this.createPageSubscription();
  }

  createPageSubscription(){
    this.interestedPropertiesSub = this.userService.getAllInterestedProperties(this.pageRequest).subscribe((pageResponse) => {
      this.responseData = pageResponse.responseData;
      this.dataSource = new MatTableDataSource<Property>(pageResponse.responseData);
      this.totalItems = pageResponse.totalItems;
      this.pageSize = pageResponse.pageSize;
    });
  }
}
