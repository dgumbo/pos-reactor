import {Injectable} from '@angular/core';
import {HttpRequest, HttpHandler, HttpEvent, HttpInterceptor} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Router} from '@angular/router';
import {AuthService} from '../services'; 

@Injectable()
export class JwtErrorInterceptor implements HttpInterceptor {
    constructor(private authenticationService: AuthService, private router: Router) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        //console.log ("interceptin error!!") ;
        return next.handle(request).pipe(
            catchError((err: any) => {
                console.log("error intercepted at ErrorInterceptor");
                console.log("err.status : ", err.status);
                console.log("err : ");
                console.log(err);
                if (err.status === 401) {
                    // auto logout if 401 response returned from api
                    this.authenticationService.logoutOnError();
                    location.reload(true);
                    this.router.navigate(['/auth/login'], {queryParams: {returnUrl: request.url}});
                }

                const error = err.error.message || err.statusText;
                //            console.trace();
                return throwError(error);
            })
        );
    }
}