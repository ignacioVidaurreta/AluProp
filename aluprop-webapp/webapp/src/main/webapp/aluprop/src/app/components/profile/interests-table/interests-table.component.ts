import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {Property} from "../../../models/property";
import {Subscription} from "rxjs";
import {UserService} from "../../../services/user.service";
import {ActivatedRoute} from "@angular/router";


@Component({
  selector: 'app-interests-table',
  templateUrl: './interests-table.component.html',
  styleUrls: ['./interests-table.component.scss']
})
export class InterestsTableComponent implements OnInit {
  displayedColumns: string[] = ['name'];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  userId: number;
  interestedPropertiesSub: Subscription;
  dataSource;

  constructor(private userService: UserService, private route: ActivatedRoute) {
    this.dataSource = [];
  }

  ngOnInit(): void {
    this.createPageSubscription();
    this.dataSource.paginator = this.paginator;
    this.userId = +this.route.snapshot.paramMap.get("id");
  }

  ngOnDestroy(): void {
    this.interestedPropertiesSub.unsubscribe();
  }

  onPageChange(pageEvent: PageEvent){
    this.interestedPropertiesSub.unsubscribe();
    this.createPageSubscription();
  }

  createPageSubscription(){
    this.interestedPropertiesSub = this.userService.getAllInterestedPropertiesByUserId(this.userId).subscribe((interestedProperties) => {
      this.dataSource = new MatTableDataSource<Property>(interestedProperties);
    });
  }
}
