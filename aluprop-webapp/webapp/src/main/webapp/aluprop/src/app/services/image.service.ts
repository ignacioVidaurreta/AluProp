import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const BASE_API_URL = 'http://localhost:8080/api/';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  constructor(private http: HttpClient) { }

  getImage(imageId: number): Observable<string>{
    return this.http.get<string>(BASE_API_URL + 'image/' + imageId);
  }
}
