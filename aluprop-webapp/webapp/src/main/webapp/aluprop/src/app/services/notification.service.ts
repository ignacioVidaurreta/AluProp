import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Notification } from 'src/app/models/notification';
import {PageRequest} from "../interfaces/page-request";
import {PageResponse} from "../interfaces/page-response";
import {Property} from "../models/property";

const BASE_API_URL = 'http://localhost:8080/api';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  unreadNotifications: Notification[];

  constructor(private http: HttpClient) { }

  getUnread(): Observable<Notification[]> {
    return this.http.get<Notification[]>(BASE_API_URL + '/notification');
  }

  getAll(pageRequest: PageRequest): Observable<PageResponse<Notification>> {
    let params = {pageSize: String(pageRequest.pageSize), pageNumber: String(pageRequest.pageNumber) };
    return this.http.get<PageResponse<Notification>>(BASE_API_URL + '/notification/all', {params: params})
  }

  changeNotificationState(notificationId: number): Observable<any>{
    console.log('Marking notif as read:');
    console.log(notificationId);
    return this.http.post(BASE_API_URL + '/notification/' + notificationId, {});
  }
}
