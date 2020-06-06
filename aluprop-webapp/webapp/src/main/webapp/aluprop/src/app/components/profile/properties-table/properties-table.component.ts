import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {Property} from "../../../models/property";
import {Subscription} from "rxjs";
import {UserService} from "../../../services/user.service";
import {ActivatedRoute} from "@angular/router";
import {UserProposal} from "../../../models/userProposal";
import {Proposal} from "../../../models/proposal";

@Component({
  selector: 'app-properties-table',
  templateUrl: './properties-table.component.html',
  styleUrls: ['./properties-table.component.scss']
})
export class PropertiesTableComponent implements OnInit {
  displayedColumns: string[] = ['name', 'state'];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  userId: number;
  ownedPropertiesSub: Subscription;
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
    this.ownedPropertiesSub.unsubscribe();
  }

  onPageChange(pageEvent: PageEvent){
    this.ownedPropertiesSub.unsubscribe();
    this.createPageSubscription();
  }

  createPageSubscription(){
    this.ownedPropertiesSub = this.userService.getAllOwnedPropertiesByUserId(this.userId).subscribe((ownedProperties) => {
      this.dataSource = new MatTableDataSource<Property>(ownedProperties);
    });
  }
}
