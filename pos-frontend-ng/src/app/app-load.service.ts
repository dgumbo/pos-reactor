import { Injectable } from '@angular/core';
import { TokenStorageService } from './auth/services';
import { Store } from '@ngrx/store';
import * as fromRoot from 'app/app.reducer';
import * as authActions from './auth/actions/';
import * as sharedActions from './shared/actions';
import { ErrorHandlerService } from './shared/errors';
import { RoleUserUnit, JwtToken } from './auth/models';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../environments/environment';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Menu } from 'shared/models/menu';

const helper = new JwtHelperService();

@Injectable()
export class AppLoadAuthStatusService {
    private url: string = environment.apiUrl + '/reports/jwt';

    constructor(private tokenStorage: TokenStorageService,
        private store: Store<fromRoot.State>,
        private errorHandler: ErrorHandlerService,
        private httpClient: HttpClient
    ) { }

    checkAuthenticationStatus(): Promise<any> {
        return new Promise((resolve, reject) => {
            //            console.log('Checking for Authentication');
            const token: string = this.tokenStorage.getToken();

            if (token) {
                const jwtToken: JwtToken = { token: token };
                const decodedToken = helper.decodeToken(token);
                //                console.log(decodedToken);
                //                console.trace();
                const roleUserUnit = decodedToken.roleUserUnit;
                if (roleUserUnit) {
                    this.store.dispatch(new authActions.SetAuthenticated(jwtToken));

                    let userMenus = decodedToken.userMenus;
                    if (userMenus) {
                        this.store.dispatch(new sharedActions.SetUserMenus(userMenus));
                    } else if (this.tokenStorage.getUserMenus()) {
                        userMenus = this.tokenStorage.getUserMenus();
                        this.store.dispatch(new sharedActions.SetUserMenus(userMenus));
                    } else {
                        this.getUserMenus(roleUserUnit);
                    }

                    this.validateToken(token);
                }
            }
            resolve();
        });
    }

    validateToken(token: string): void {
        // const expirationDate = helper.getTokenExpirationDate(token);
        const isExpired = helper.isTokenExpired(token);

        if (isExpired) {
            this.tokenStorage.signOut();
            this.store.dispatch(new authActions.SetUnAuthenticated);
        }
    }

    getUserMenus(roleUserUnit: RoleUserUnit): void {
        //        console.log("Auto - getting user menus !!");
        this.store.dispatch(new sharedActions.IsLoading);
        const role = roleUserUnit.role;

        let headers = new HttpHeaders();
        headers = headers.set('Content-Type', 'application/json; charset=utf-8');
        this.httpClient.post(this.url + '/getUserMenus', role, {
            headers: headers
        })
            .subscribe(
                (menus: Menu[]) => {
                    //                    console.log(menus);
                    this.tokenStorage.saveUserMenus(menus);
                    this.store.dispatch(new sharedActions.SetUserMenus(menus));
                    this.store.dispatch(new sharedActions.FinishedLoading);
                },
                (error: Error) => this.errorHandler.handleError(error)
            );
    }
}
