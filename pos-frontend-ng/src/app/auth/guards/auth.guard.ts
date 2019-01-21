import { Injectable } from '@angular/core';
import {
    CanActivate,
    ActivatedRouteSnapshot,
    RouterStateSnapshot,
    CanLoad,
    Route,
    Router
} from '@angular/router';
import { Store, select } from '@ngrx/store';
import * as fromRoot from 'app/app.reducer';
import { Observable } from 'rxjs';
import { take } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material';

@Injectable()
export class AuthGuard implements CanActivate, CanLoad {
    constructor(private store: Store<fromRoot.State>,
        private router: Router,
        private snackbar: MatSnackBar, ) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Observable<boolean> | Promise<boolean> {
        const loginRoute = '/auth/login';
        const activateUrl = state.url || '/';

        const observable = this.store.pipe(
            take(1),
            select(fromRoot.getIsAuthenticated),
        );

        //        redirect to sign in page if user is not authenticated
        observable.subscribe(authenticated => {
            // console.log('can activate auth guard. authenticated : ' + authenticated);
            if (!authenticated) {
                this.router.navigate([loginRoute], { queryParams: { returnUrl: activateUrl } });
                this.notifyForLogin('Please Login !');
            }
        });

        return observable;
    }

    canLoad(route: Route): boolean | Observable<boolean> | Promise<boolean> {
        const loginRoute = '/auth/login';
        const activateUrl = route.outlet || '/';

        const observable = this.store.pipe(
            take(1),
            select(fromRoot.getIsAuthenticated),
        );

        //        redirect to sign in page if user is not authenticated
        observable.subscribe(authenticated => {
            // console.log('can load auth gard. authenticated : ' + authenticated);
            if (!authenticated) {
                this.router.navigate([loginRoute], { queryParams: { returnUrl: activateUrl } });
                this.notifyForLogin('Please Login !');
            }
        });

        return observable;
    }

    notifyForLogin(loginMsg: string) {
        this.snackbar.open(loginMsg, null, {
            duration: 6000
        });
    }
}
