import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Notification } from 'src/app/models/notification';

const BASE_API_URL = 'http://localhost:8080/api';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private http: HttpClient) { }

  getUnread(): Observable<Notification[]> {
    return this.http.get<Notification[]>(BASE_API_URL + '/notification');
  }

  getAll(): Observable<Notification[]> {
    return this.http.get<Notification[]>(BASE_API_URL + '/notification');
  }
}
