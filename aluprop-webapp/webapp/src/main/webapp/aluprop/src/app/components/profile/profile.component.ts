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

  pageRequest: PageRequest;
  pageResponse: PageResponse<User>;

  user: User;
  userSub: Subscription;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    console.log('leel');
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
    this.userSub = this.userService.getAll(this.pageRequest).subscribe((pageResponse) => {
      console.log(pageResponse);
      this.user = pageResponse.data[0];
    });
  }

}

// export interface Properties {
//   name: string;
//   availability: string;
// }
//
// const ELEMENT_DATA_PROPERTIES: Properties[] = [
//   {name: 'Bonita casa en Punta Chica', availability: 'available'},
//   {name: 'Loft con vista al Rio', availability: 'available'},
//   {name: 'Departamento moderno', availability: 'unavailable'},
//   {name: 'Bonita casa en Punta Chica', availability: 'available'},
//   {name: 'Loft con vista al Rio', availability: 'available'},
//   {name: 'Bonita casa en Punta Chica', availability: 'unavailable'},
//   {name: 'Loft con vista al Rio', availability: 'unavailable'},
//   {name: 'Departamento moderno', availability: 'available'},
//   {name: 'Bonita casa en Punta Chica', availability: 'available'},
//   {name: 'Loft con vista al Rio', availability: 'available'},
// ];
//
//
// export interface Proposals {
//   name: string;
//   creatorId: number;
//   state: string;
// }
//
// const ELEMENT_DATA_PROPOSALS: Proposals[] = [
//   {name: 'Bonita casa en Punta Chica', creatorId: 1, state: 'sent'},
//   {name: 'Loft con vista al Rio', creatorId: 2, state: 'accepted'},
//   {name: 'Departamento moderno',  creatorId: 1, state: 'declined'},
//   {name: 'Bonita casa en Punta Chica', creatorId: 1, state: 'dropped'},
//   {name: 'Loft con vista al Rio', creatorId: 2, state: 'sent'},
//   {name: 'Departamento moderno',  creatorId: 1, state: 'sent'},
// ];
