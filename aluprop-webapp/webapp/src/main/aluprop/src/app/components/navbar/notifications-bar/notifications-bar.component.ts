import { Component, OnInit, HostListener } from '@angular/core';
import {take} from "rxjs/operators";
import {NotificationService} from "../../../services/notification.service";

@Component({
  selector: 'app-notifications-bar',
  templateUrl: './notifications-bar.component.html',
  styleUrls: ['./notifications-bar.component.scss']
})
export class NotificationsBarComponent implements OnInit {

  showNotifications = false;
  badgeNumber = 0;
  hidden = true;

  @HostListener('document:click', ['$event'])
  onDocumentClick(event) {
    if (this.showNotifications === true && event.path.filter(
        (elem)=> elem.id === 'toggle-notifications').length !== 1){
      this.showNotifications = false;
    }
  }

  constructor(private notificationService: NotificationService,) { }

  ngOnInit(): void {
    this.createPageSubscription();
  }

  createPageSubscription() {
    this.notificationService.getUnread().pipe(take(1)).subscribe((notifications) => {
      if(notifications.length > 0) {
        this.badgeNumber = notifications.length;
        this.hidden = false;
      }
      else {
        this.hidden = true;
      }
    });
  }

  updateBadge() {
    this.showNotifications = false;
    this.createPageSubscription();
  }

  toggleBadgeVisibility() {
    this.hidden = !this.hidden;
  }

  toggleNotifications(){
    this.showNotifications = !this.showNotifications;
  }

}
