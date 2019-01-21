import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { User, JwtToken, RoleUserUnit } from '../models';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { environment } from 'environments/environment';
import * as fromRoot from 'app/app.reducer';
import * as  AuthActions from '../actions/';
import * as  SharedActions from '../../shared/actions';
import { Store } from '@ngrx/store';
import { TokenStorageService } from './token-storage.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Menu } from 'shared/models/menu';

const helper = new JwtHelperService();

@Injectable()
export class AuthService {
    private headers: HttpHeaders;
    private url: string;

    constructor(private httpClient: HttpClient,
        private router: Router,
        private tokenStorage: TokenStorageService,
        private store: Store<fromRoot.State>) {

        this.url = environment.apiUrl + '/reports/jwt';
        this.headers = new HttpHeaders();
        this.headers = this.headers.set('Content-Type', 'application/json; charset=utf-8');

        this.checkLoggedInStatusOnRefresh();
    }

    checkLoggedInStatusOnRefresh() {
        // console.log('this.tokenStorage.getToken() : ', this.tokenStorage.getToken());
        // this.store.dispatch(new AuthActions.SetAuthenticated({role:
        // {name: '', userLevel: ''}, user: {username: ''}, unit: {name: '', unitCode: ''}}));
    }

    login(userData: User, returnUrl: string): void {
        this.httpClient.post(this.url + '/login', JSON.stringify(userData), {
            headers: this.headers
        })
            .subscribe(
                (token: JwtToken) => {
                    this.preAuthSuccessfull(returnUrl, token);
                },
                (error: Error) => this.handleError(error)
            );
    }

    setRoleUserUnit(roleUserUnit: RoleUserUnit, returnUrl: string): void {
        this.store.dispatch(new SharedActions.IsLoading);
        const jwtToken: JwtToken = { token: this.tokenStorage.getPreAuthToken() };

        const holderParams: { jwtAuthToken?: JwtToken, roleUserUnit?: RoleUserUnit } = {};
        holderParams.jwtAuthToken = jwtToken;
        holderParams.roleUserUnit = roleUserUnit;

        this.httpClient.post(this.url + '/setroleunit', holderParams, {
            headers: this.headers
        })
            .subscribe(
                (token: JwtToken) => {
                    this.fullAuthSuccessfull(returnUrl, token);

                    const decodedToken = helper.decodeToken(token.token);
                    const userMenus = decodedToken.userMenus;
                    if (userMenus) {
                        this.store.dispatch(new SharedActions.SetUserMenus(userMenus));
                    }                    else {
                        this.getUserMenus(roleUserUnit);
                    }
                },
                (error: Error) => this.handleError(error)
            );
    }

    getUserMenus(roleUserUnit: RoleUserUnit): void {
        this.store.dispatch(new SharedActions.IsLoading);
        const role = roleUserUnit.role;
        this.httpClient.post(this.url + '/getUserMenus', role, {
            headers: this.headers
        })
            .subscribe(
                (menus: Menu[]) => {
                    // console.log(menus);
                    this.tokenStorage.saveUserMenus(menus);
                    this.store.dispatch(new SharedActions.SetUserMenus(menus));
                    this.store.dispatch(new SharedActions.FinishedLoading);
                },
                (error: Error) => this.handleError(error)
            );
    }

    getUserRoleUnits() {
        const jwtToken: JwtToken = { token: this.tokenStorage.getPreAuthToken() };
        return this.httpClient.post(this.url + '/getroleuserunits', JSON.stringify(jwtToken), {
            headers: this.headers
        });
    }

    logout(): void {
        this.tokenStorage.signOut();
        this.store.dispatch(new AuthActions.SetUnAuthenticated());
        this.store.dispatch(new SharedActions.UnSetMenus());
        this.router.navigate(['/']);
    }

    logoutOnError(): void {
        this.tokenStorage.signOut();
        this.store.dispatch(new AuthActions.SetUnAuthenticated());
    }

    private preAuthSuccessfull(returnUrl: string, token: JwtToken): void {
        //        console.log('preAuthSuccessfull\n', 'returnUrl\n', returnUrl);
        this.tokenStorage.savePreAuthToken(token.token);
        this.router.navigate(['/auth/roleunit'], { queryParams: { returnUrl: returnUrl } });
    }

    private fullAuthSuccessfull(returnUrl: string, jwtToken: JwtToken): void {
        const decodedToken = helper.decodeToken(jwtToken.token);
        const roleUserUnit: RoleUserUnit = decodedToken.roleUserUnit;

        this.tokenStorage.saveToken(jwtToken.token);
        this.tokenStorage.saveRoleUserMenu(roleUserUnit);
        this.store.dispatch(new AuthActions.SetAuthenticated(jwtToken));

        this.router.navigate([returnUrl]);
    }

    autoLoginWithToken(returnUrl: string, jwtToken: JwtToken, ): void {

        this.fullAuthSuccessfull(returnUrl, jwtToken);
    }

    handleError(error: Error): void {
        console.log('error');
        console.log(error);
    }
}
