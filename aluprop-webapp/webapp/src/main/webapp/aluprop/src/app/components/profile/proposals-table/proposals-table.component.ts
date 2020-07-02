import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {Proposal} from "../../../models/proposal";
import {UserProposal} from "../../../models/userProposal";
import {Subscription} from "rxjs";
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../../services/user.service";


@Component({
  selector: 'app-proposals-table',
  templateUrl: './proposals-table.component.html',
  styleUrls: ['./proposals-table.component.scss']
})
export class ProposalsTableComponent implements OnInit {
  displayedColumns: string[] = ['name','ownership'];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  @Input() userRole: string;

  userId: number;
  proposalsSub: Subscription;
  dataSource;

  constructor(private userService: UserService, private route: ActivatedRoute) {
    this.dataSource = [];
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
    this.proposalsSub.unsubscribe();
    this.createPageSubscription();
  }

  createPageSubscription(){
    if(this.userRole == "ROLE_GUEST") {
      this.proposalsSub = this.userService.getAllUserProposals().subscribe((userProposals) => {
        console.log(userProposals);
        this.dataSource = new MatTableDataSource<UserProposal>(userProposals);
      });
    }
    else {
      this.proposalsSub = this.userService.getAllProposals().subscribe((proposals) => {
        console.log(proposals);
        this.dataSource = new MatTableDataSource<Proposal>(proposals);
      });
    }
  }
}
