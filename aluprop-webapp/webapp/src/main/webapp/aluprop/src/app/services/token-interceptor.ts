// src/app/auth/token.interceptor.ts
import { Injectable, Injector } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpResponse,
  HttpErrorResponse
} from '@angular/common/http';
import { AuthenticationService } from './authentication.service';
import { Observable, EMPTY } from 'rxjs';
import { tap, filter, catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(private authService: AuthenticationService,
              private router: Router) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.authService.getAuthToken()){
      // console.log(request);
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.authService.getAuthToken()}`
        }
      });
    }
    
    return next.handle(request).pipe(catchError(
      (response: HttpErrorResponse) => {
        if (response.status >= 404 && response.status < 500){
          this.router.navigate(['/error/404']);
        }
        if (response.status >= 500 && response.status < 600){
          this.router.navigate(['/error/500']);
        }
        return Observable.throw(response);
      })
    );
  }
}