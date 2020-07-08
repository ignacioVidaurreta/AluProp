import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, from } from 'rxjs';
import { switchMap } from 'rxjs/operators';

const BASE_API_URL = 'http://localhost:8080/api/';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  constructor(private http: HttpClient) { }

  getImage(imageId: number): Observable<any>{
    return this.http.get<Blob>(BASE_API_URL + 'image/' + imageId, {observe: 'body', responseType: 'blob' as 'json'}).pipe(
      switchMap(
        val => from( // create the observable from a promise
          new Promise((resolve, reject) => { //create a new Promise                          
            const reader = new FileReader();
            reader.readAsDataURL(val);
            reader.onloadend = () => resolve(reader.result); //resolve when it finishes to load the file
            reader.onerror = () => reject(reader.error); //rejects if there was an error while reading the file
          })
        )
      )
    );
  }
}
