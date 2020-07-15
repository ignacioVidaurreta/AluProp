import { Component, OnInit, ViewChild } from '@angular/core';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import { TranslateService } from '@ngx-translate/core';
import { NotificationService } from 'src/app/services/notification.service';
import { Subscription } from 'rxjs';
import { MatTableDataSource } from '@angular/material/table';
import { Notification } from 'src/app/models/notification';
import { take } from 'rxjs/operators';
import { ActivatedRoute, Router } from '@angular/router';
import {PageRequest} from "../../interfaces/page-request";
import {Property} from "../../models/property";
import {PropertyService} from "../../services/property.service";

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
  totalItems: number;
  pageSize: number;

  pageRequest: PageRequest;

  constructor(private notificationService: NotificationService,
              private propertyService: PropertyService,
              private translateService: TranslateService,
              private router: Router) {
      this.languageChangedSub = translateService.onLangChange.subscribe((newLang) => this.updateNotificationText());
      this.dataSource = [];
      this.pageRequest = {pageNumber: 0, pageSize: 10}
    }

  ngOnInit(): void {
    this.dataSource.paginator = this.paginator;
    this.createPageSubscription();
  }

  createPageSubscription() {
    this.notificationsSub = this.notificationService.getAll(this.pageRequest).subscribe((pageResponse) => {
      this.notifications = pageResponse.responseData;
      this.dataSource = new MatTableDataSource<Notification>(pageResponse.responseData);
      this.totalItems = pageResponse.totalItems;
      this.pageSize = pageResponse.pageSize;
      this.updateNotificationText();
    });
  }

  onPageChange(pageEvent: PageEvent){
    this.pageRequest.pageNumber = pageEvent.pageIndex;
    this.pageRequest.pageSize = pageEvent.pageSize;
    if (this.languageChangedSub){ this.languageChangedSub.unsubscribe(); }
    this.createPageSubscription();
  }

  ngOnDestroy(): void {
    if (this.languageChangedSub){ this.languageChangedSub.unsubscribe(); }
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

    this.dataSource = new MatTableDataSource<Notification>(this.notifications);
  }

  navigateTo(link: string) {
    this.router.navigate([link]);
  }

  checkUnread(notificationId: number){
    return this.notificationService.unreadNotifications?.map(function(notification) { return notification?.id; }).includes(notificationId);
  }
}
