import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-notifications-bar',
  templateUrl: './notifications-bar.component.html',
  styleUrls: ['./notifications-bar.component.scss']
})
export class NotificationsBarComponent implements OnInit {

  showNotifications = false;


  constructor() { }

  ngOnInit(): void {
  }

  toggleNotifications(){
    this.showNotifications = !this.showNotifications;
  }

}
