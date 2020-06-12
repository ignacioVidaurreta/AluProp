import { Component, OnInit } from '@angular/core';
import { Notification } from 'src/app/models/notification';
import { Subscription } from 'rxjs';
import { take } from 'rxjs/operators';
import { NotificationService } from 'src/app/services/notification.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-notifications-menu',
  templateUrl: './notifications-menu.component.html',
  styleUrls: ['./notifications-menu.component.scss']
})
export class NotificationsMenuComponent implements OnInit {

  notifications: Notification[];
  notificationsSub: Subscription;

  languageChangedSub: Subscription;

  constructor(private notificationService: NotificationService,
    private translateService: TranslateService) {
      this.languageChangedSub = translateService.onLangChange.subscribe((newLang) => this.updateNotificationText());
    }

  ngOnInit(): void {
    this.notificationsSub = this.notificationService.getUnread().subscribe((notifications) => {
      this.notifications = notifications;
      this.updateNotificationText();
    });
  }

  ngOnDestroy(): void {
    if (this.languageChangedSub){ this.languageChangedSub.unsubscribe(); }
  }

  updateNotificationText(){
    if (!this.notifications){
      return;
    }
    this.notifications.forEach((notification) => {
      this.translateService.get(notification.subjectCode).pipe(take(1)).subscribe((value) => notification.subject = value);
      this.translateService.get(notification.textCode).pipe(take(1)).subscribe((value) => notification.text = value);
    })
  }

}
