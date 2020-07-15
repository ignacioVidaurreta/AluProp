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
import {Observable, EMPTY, throwError} from 'rxjs';
import { tap, filter, catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import {TranslateService} from "@ngx-translate/core";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(private authService: AuthenticationService,
              private translateService: TranslateService,
              private router: Router) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.authService.getAuthToken()){
      request = request.clone({
        setHeaders: {
            "Authorization": `Bearer ${this.authService.getAuthToken()}`,
            "Accept-Language": `${this.translateService.currentLang}`
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
        return throwError(response);
      })
    );
  }
}
