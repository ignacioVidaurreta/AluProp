import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { TranslateService } from '@ngx-translate/core';
import { NotificationService } from 'src/app/services/notification.service';
import { Subscription } from 'rxjs';
import { MatTableDataSource } from '@angular/material/table';
import { Notification } from 'src/app/models/notification';
import { take } from 'rxjs/operators';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss']
})
export class NotificationsComponent implements OnInit {

  notifications: Notification[];
  notificationsSub: Subscription;

  languageChangedSub: Subscription;

  dataSource;
  displayedColumns: string[] = ['name'];
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  constructor(private notificationService: NotificationService,
              private translateService: TranslateService,
              private router: Router) {
      this.languageChangedSub = translateService.onLangChange.subscribe((newLang) => this.updateNotificationText());
      this.dataSource = [];
    }

  ngOnInit(): void {
    this.notificationsSub = this.notificationService.getAll().subscribe((notifications) => {
      this.notifications = notifications;
      console.log(this.notifications);

      this.dataSource = new MatTableDataSource<Notification>(this.notifications);
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
      this.translateService.get(notification.subjectCode).pipe(take(1)).subscribe((value) => notification.translatedSubject = value);
      this.translateService.get(notification.textCode + ".text").pipe(take(1)).subscribe((value) => notification.translatedText = value);
    })

    this.dataSource = new MatTableDataSource<Notification>(this.notifications);
  }

  navigateTo(link: string) {
    console.log(link);
    this.router.navigate([link]);
  }
}
