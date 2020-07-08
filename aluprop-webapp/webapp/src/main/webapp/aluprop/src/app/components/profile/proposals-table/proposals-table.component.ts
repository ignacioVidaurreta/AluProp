import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {Proposal} from "../../../models/proposal";
import {UserProposal} from "../../../models/userProposal";
import {Subscription} from "rxjs";
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../../services/user.service";
import {PageRequest} from "../../../interfaces/page-request";
import {Property} from "../../../models/property";


@Component({
  selector: 'app-proposals-table',
  templateUrl: './proposals-table.component.html',
  styleUrls: ['./proposals-table.component.scss']
})
export class ProposalsTableComponent implements OnInit {
  displayedColumns: string[] = ['name','ownership'];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  totalItems: number;
  pageSize: number;

  pageRequest: PageRequest;

  @Input() userRole: string;

  userId: number;
  proposalsSub: Subscription;
  dataSource;

  constructor(private userService: UserService, private route: ActivatedRoute) {
    this.dataSource = [];
    this.pageRequest = {pageNumber: 0, pageSize: 10}
  }

  ngOnInit(): void {
    this.dataSource.paginator = this.paginator;
    this.userId = +this.route.snapshot.paramMap.get("id");
    this.createPageSubscription();
  }

  ngOnDestroy(): void {
    this.proposalsSub.unsubscribe();
  }

  onPageChange(pageEvent: PageEvent){
    this.pageRequest.pageNumber = pageEvent.pageIndex;
    this.pageRequest.pageSize = pageEvent.pageSize;
    this.proposalsSub.unsubscribe();
    this.createPageSubscription();
  }

  createPageSubscription(){
    if(this.userRole == "ROLE_GUEST") {
      console.log(this.pageRequest);
      this.proposalsSub = this.userService.getAllProposalsFromUserProposals(this.pageRequest).subscribe((pageResponse) => {
        console.log(pageResponse);
        this.dataSource = new MatTableDataSource<Proposal>(pageResponse.responseData);
        this.totalItems = pageResponse.totalItems;
        this.pageSize = pageResponse.pageSize;
      });
    }
    else {
      console.log(this.pageRequest);
      this.proposalsSub = this.userService.getAllProposals(this.pageRequest).subscribe((pageResponse) => {
        console.log(pageResponse);
        this.dataSource = new MatTableDataSource<Proposal>(pageResponse.responseData);
        this.totalItems = pageResponse.totalItems;
        this.pageSize = pageResponse.pageSize;
      });
    }
  }
}
