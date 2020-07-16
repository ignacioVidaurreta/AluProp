import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import { Notification } from 'src/app/models/notification';
import {forkJoin, Observable, Subscription} from 'rxjs';
import { take } from 'rxjs/operators';
import { NotificationService } from 'src/app/services/notification.service';
import { TranslateService } from '@ngx-translate/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-notifications-menu',
  templateUrl: './notifications-menu.component.html',
  styleUrls: ['./notifications-menu.component.scss']
})
export class NotificationsMenuComponent implements OnInit {

  notifications: Notification[];
  notificationsSub: Subscription;

  @Output() readNotification = new EventEmitter();
  @Output() closeMenu = new EventEmitter();

  languageChangedSub: Subscription;

  constructor(private notificationService: NotificationService,
              private router: Router,
              private translateService: TranslateService) {
      this.languageChangedSub = translateService.onLangChange.subscribe((newLang) => this.updateNotificationText());
    }

  ngOnInit(): void {
    this.createPageSubscription();
  }

  createPageSubscription() {
    this.notificationsSub = this.notificationService.getUnread().subscribe((notifications) => {
      this.notifications = notifications;
      this.updateNotificationText();
    });
  }

  ngOnDestroy(): void {
    if (this.languageChangedSub){ this.languageChangedSub.unsubscribe(); }
    if (this.notificationsSub){ this.notificationsSub.unsubscribe(); }
  }

  onPageChange() {
    if (this.languageChangedSub){ this.languageChangedSub.unsubscribe(); }
    if (this.notificationsSub){ this.notificationsSub.unsubscribe(); }
    this.createPageSubscription();
  }

  updateNotificationText(){
    if (!this.notifications){
      return;
    }
    this.notifications.forEach((notification) => {
      let propertyName: string;
      propertyName = notification.proposal.property.description;
      this.translateService.get(notification.subjectCode, {propertyName: propertyName}).pipe(take(1)).subscribe((value) => notification.translatedSubject = value);
      this.translateService.get(notification.textCode + ".text").pipe(take(1)).subscribe((value) => notification.translatedText = value);
    })
  }

  markRead(notification: Notification) {
    this.notificationService.changeNotificationState(notification.id).subscribe(
      () => {
        this.readNotification.emit();
        this.onPageChange();
      });
    this.closeMenu.emit();
    this.router.navigate([notification.link]);
  }

  viewAll() {
    this.closeMenu.emit();
    if(this.notifications.length == 0) {
      this.readNotification.emit();
      this.router.navigate(['/notifications']);
    }
    else {
      let obs: Observable<any>[] = [];
      this.notifications.forEach((notification) => {
        obs.push(this.notificationService.changeNotificationState(notification.id));
      })
      forkJoin(obs).subscribe(() => {
        this.readNotification.emit();
        this.onPageChange();
        this.notificationService.unreadNotifications = this.notifications;
      })
      this.router.navigate(['/notifications']);
    }
  }

}
