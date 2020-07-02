import { Component, OnInit, ViewChild } from '@angular/core';
import {PageRequest} from "../../interfaces/page-request";
import {PageResponse} from "../../interfaces/page-response";
import {Subscription} from "rxjs";
import {User} from "../../models/user";
import {UserService} from "../../services/user.service";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {ProposalService} from "../../services/proposal.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  userId: number;
  user: User;
  userSub: Subscription;

  constructor(private userService: UserService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.userId = +this.route.snapshot.paramMap.get("id");
    this.createPageSubscription();
  }

  ngOnDestroy(): void {
    this.userSub.unsubscribe();
  }

  onPageChange(pageEvent: PageEvent){
    this.userSub.unsubscribe();
    this.createPageSubscription();
  }

  createPageSubscription(){
    this.userSub = this.userService.getUserById(this.userId).subscribe((user) => {
      this.user = user;
      console.log(this.user);
    });
  }

  date(birthDate: Date) {
    return new Date(birthDate).toLocaleDateString();
  }
}
