import { Component, OnInit, ViewChild } from '@angular/core';
import {PageRequest} from "../../interfaces/page-request";
import {PageResponse} from "../../interfaces/page-response";
import {Subscription} from "rxjs";
import {User} from "../../models/user";
import {UserService} from "../../services/user.service";
import {MatPaginator, PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  totalItems: number;
  pageSize: number;

  pageRequest: PageRequest;
  pageResponse: PageResponse<User>;

  user: User;
  userSub: Subscription;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.createPageSubscription();
  }

  ngOnDestroy(): void {
    this.userSub.unsubscribe();
  }

  onPageChange(pageEvent: PageEvent){
    this.pageRequest.pageNumber = pageEvent.pageIndex;
    this.pageRequest.pageSize = pageEvent.pageSize;
    this.userSub.unsubscribe();
    this.createPageSubscription();
  }

  createPageSubscription(){
    this.userSub = this.userService.getAll(this.pageRequest).subscribe((pageResponse) => {
      console.log(pageResponse);
      this.user = pageResponse.data[0];
      this.totalItems = pageResponse.totalItems;
      this.pageSize = pageResponse.pageSize;
    });
  }

}
