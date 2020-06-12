import { Component, OnInit } from '@angular/core';
import { Notification } from 'src/app/models/notification';
import { Subscription } from 'rxjs';
import { NotificationService } from 'src/app/services/notification.service';

@Component({
  selector: 'app-notifications-menu',
  templateUrl: './notifications-menu.component.html',
  styleUrls: ['./notifications-menu.component.scss']
})
export class NotificationsMenuComponent implements OnInit {

  notifications: Notification[];
  notificationsSub: Subscription;

  constructor(private notificationService: NotificationService) { }

  ngOnInit(): void {
    this.notificationsSub = this.notificationService.getUnread().subscribe((notifications) => this.notifications = notifications);
  }

}
